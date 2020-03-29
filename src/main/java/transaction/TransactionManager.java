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

/**
 *
 * @author scott
 */
public class TransactionManager implements MessageTypes {
    
    private static final ArrayList<Transaction> transactions = new ArrayList<>();;
    private static int transactionCounter = 0;
    
    public TransactionManager() {
    }
    
    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }
    
    public void runTransaction(Socket client) {
        (new TransactionManagerWorker(client)).start();  
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
        int accountId = 0;
        int balance = 0;
        
        // flag for jumping out of while loop after this transaction closed
        boolean keepGoing = true;
        
        private TransactionManagerWorker(Socket client)
        {
            this.client = client;
            
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
                        
                        synchronized (transactions) {
                            transaction = new Transaction(transactionCounter++);
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
                }
            }
        }
    }
}
