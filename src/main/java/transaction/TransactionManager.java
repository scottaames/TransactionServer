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
    
    private static final ArrayList<Transaction> transactions = new ArrayList<>();;
    private static int transactionCounter = 0;
    ReentrantLock r_lock = new ReentrantLock();
    
    public TransactionManager() {
    }
    
    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }
    
    public void runTransaction(Socket client) {
        (new TransactionManagerWorker(client, r_lock )).start();  
    }
    
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
            this.client = client;
            this.r_lock = r_lock; 
            
            try 
            {
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
            while (keepGoing) 
            {
                // reading msg
                try
                {
                    message = (Message) readFromNet.readObject();
                } catch (IOException | ClassNotFoundException e)
                {
                    System.out.println("[TransactionManagerWorker.run] Message could not be read from object stream.");
                    System.exit(1);
                }
                
                switch (message.getType())
                {
                    case OPEN_TRANSACTION:
                        
                        // 
                        r_lock.lock(); 
                        try {
                            transId = transactionCounter;
                            transactionCounter++; 
                        } finally {
                            r_lock.unlock(); 
                        }
                        
                        synchronized (transactions) {
                            
                            transaction = new Transaction(transId);
                            transactions.add(transaction);
                        }
                        
                        try {
                            writeToNet.writeObject(transaction.getID());
                        } catch (IOException e) {
                            System.out.println("[TransactionManagerWorker.run] OPEN_TRANSACTION - Error when writing transactionID");
                        }
                        //transaction.log("[TransactionManagerWorker.run] OPEN_TRANSACTION #" + transaction.getID());
                        break;
                    
                    case CLOSE_TRANSACTION:
                        
                        TransactionServer.lockManager.unLock(transaction);
                        transactions.remove(transaction);
                        
                        try {
                            readFromNet.close();
                            writeToNet.close();
                            client.close();
                            keepGoing = false;
                        } catch (IOException e) {
                            System.out.println("[TransactionManagerWorker.run] CLOSE_TRANSACTION - Error when closing connection with client");  
                        }
                        
                        //transaction.log("[TransactionManagerWorker.run] CLOSE_TRANSACTION #" + transaction.getID());
                        
                        if (TransactionServer.transactionView) {
                            System.out.println(transaction.getLog());
                        }
                        
                        break; 
                    
                    case READ_REQUEST: 
                        
                        try {
                            // Find the account with the same ID that we're searching for 
                            account = TransactionServer.accountManager.getAccount( (int) message.content ); 
                            writeToNet.writeObject( account.getBalance() ); 
                        } catch (IOException e ) {
                            System.out.println( "Failed to find account or access its balance" ); 
                        }
                        
                        break; 
                        
                    case WRITE_REQUEST: 
                        
                        // Find the account we want to access 
                        account = TransactionServer.accountManager.getAccount( (int) message.content ); 
                            
                        // Change its balance 
                        account.setBalance( (int) message.amount ); 
                }
            }
        }
    }
}
