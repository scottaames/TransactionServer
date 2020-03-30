/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock; 

/**
 *
 * @author scott
 */
public class TransactionManager implements MessageTypes {
    
    private static final ArrayList<Transaction> transactions = new ArrayList<>();
    private static int transactionCounter = 0;
    ReentrantLock r_lock = new ReentrantLock();
    
    public TransactionManager() {
    }
    
    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }
    
    /**
     * Creates a worker thread that will handle everything and runs it 
     * @param client The socket connection to the client 
     */
    public void runTransaction(Socket client) {
        (new TransactionManagerWorker(client, r_lock )).start();  
    }
    
    /**
     * Does the bulk of the work of a transaction. Handles input from the network and sends back information
     */
    public class TransactionManagerWorker extends Thread 
    {
        // networking communication 
        Socket client = null;
        ObjectInputStream readFromNet = null;
        ObjectOutputStream writeToNet = null;
        Message message = null;
        
        // transaction related
        Transaction transaction = null;
        Account account; 
        int accountId = 0;
        int balance = 0; 
        int transId; 
        ReentrantLock r_lock; 
        
        // flag for jumping out of while loop after this transaction closed
        boolean keepGoing = true;
        
        private TransactionManagerWorker(Socket client, ReentrantLock r_lock )
        {
            
            // The socket connection to the client 
            this.client = client;
            
            // Look that keeps ID creation for transactions from having race conditions 
            this.r_lock = r_lock; 
            
            try 
            {
                
                // Create the input and output streams to and from the client 
                readFromNet = new ObjectInputStream(client.getInputStream());
                writeToNet = new ObjectOutputStream(client.getOutputStream());
            } catch (IOException e) {
                System.out.println("[TransactionManagerWorker.run] Failed to open object streams");
                e.printStackTrace();
                System.exit(1);
            }
        }
        
        @Override
        public void run()
        {
            
            // Continue listening for messages from the client until the transaction is closed 
            while (keepGoing) 
            {
                // reading msg
                try
                {
                    
                    // Read the message from the client 
                    message = (Message) readFromNet.readObject();
                } catch (IOException | ClassNotFoundException e)
                {
                    System.out.println("[TransactionManagerWorker.run] Message could not be read from object stream.");
                    System.exit(1);
                }
                
                // Determine the type of message that has been received 
                switch (message.getType())
                {
                    
                    // If the message wants to open a transaction 
                    case OPEN_TRANSACTION:
                        
                        // Create a unique id for the transaction without a race condition 
                        r_lock.lock(); 
                        try {
                            
                            // Get the current transCounter and save it to give to the transaction 
                            transId = transactionCounter;
                            
                            // Increment the counter 
                            transactionCounter++; 
                        } finally {
                            
                            // Allow the next transaction to get an ID
                            r_lock.unlock(); 
                        }
                        
                        // Prevent race conditions for the transactions
                        synchronized (transactions) {
                            
                            // Create our new transaction with its ID
                            transaction = new Transaction(transId);
                            
                            // Add this transaction to the list of transactions 
                            transactions.add(transaction);
                        }
                        
                        try {
                            
                            // Send the client the id of its transaction 
                            writeToNet.writeObject(transaction.getID());
                        } catch (IOException e) {
                            System.out.println("[TransactionManagerWorker.run] OPEN_TRANSACTION - Error when writing transactionID");
                        }
                        transaction.log("[TransactionManagerWorker.run] OPEN_TRANSACTION #" + transaction.getID());
                        break;
                    
                    // If the message wants to close the connection to the server 
                    case CLOSE_TRANSACTION:
                        
                        // Release all of the locks held by the transaction 
                        TransactionServer.lockManager.unLock(transaction);
                        
                        // Remove the transaction from the list of transactions 
                        transactions.remove(transaction);
                        
                        try {
                            
                            // Close all connections to the client 
                            readFromNet.close();
                            writeToNet.close();
                            client.close();
                            
                            // End the while loop 
                            keepGoing = false;
                        } catch (IOException e) {
                            System.out.println("[TransactionManagerWorker.run] CLOSE_TRANSACTION - Error when closing connection with client");  
                        }
                        
                        transaction.log("[TransactionManagerWorker.run] CLOSE_TRANSACTION #" + transaction.getID());
                        
                        // Now that the transaction is done, print all of its logs if so desired
                        if (TransactionServer.transactionView) {
                            System.out.println(transaction.getLog());
                        }
                        
                        break; 
                    
                    // If the client wants to read an accounts balance 
                    case READ_REQUEST: 
                        
                        // Get the desired account ID from the message from the client 
                        accountId =  (int) message.getContent(); 
                        transaction.log("[TransactionManagerWorker.run] READ_REQUEST >>>>>>>>>>>>>>>>>>>>> account # " + accountId);
                        
                        // Save the balance from the account 
                        balance = TransactionServer.accountManager.read(accountId, transaction);
                        
                        try { 
                            
                            // Write the balance back to the client 
                            writeToNet.writeObject( (int) balance); 
                        } catch (IOException e ) {
                            System.out.println("[TransactionManagerWorker.run]  READ_REQUEST - Error when writing to object stream");
                        }
                        
                        transaction.log("[TransactionManagerWorker.run] READ_REQUEST <<<<<<<<<<<<<<<<<<<<<<<<<<<<< account #" + accountId + ", balance of $" + balance);
                        
                        break; 
                        
                    /**
                     * If the client wants to write a new balance for an account 
                     */
                    case WRITE_REQUEST: 
                        
                        // Find the account we want to access 
                        String msg = (String) message.getContent();
                        
                        // Split the contents of the message into two parts, the account id and the new balance 
                        String[] parts = msg.split(",");
                        
                        // Convert our accoutnID into an int 
                        accountId = Integer.parseInt(parts[0]);
                        transaction.log("[TransactionManagerWorker.run] WRITE_REQUEST >>>>>>>>>>>>>>>>>>>>>>> account # " + accountId);
                        
                        // Find the account we want to change and change its balance to the one received from the client 
                        TransactionServer.accountManager.write(accountId, transaction, Integer.parseInt(parts[1]));
                        
                        transaction.log("[TransactionManagerWorker.run] WRITE_ERQUEST >>>>>>>>>>>>>>>>>>>>>>> account # " + accountId + ", new balance is $" + balance);
                        break;
                }
            }
        }
    }
}
