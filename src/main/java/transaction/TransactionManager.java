/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

/**
 *
 * @author scott
 */
public class TransactionManager {
    
    
    
    public TransactionManager() {
        
        
    }
    
    public void addTransaction( Socket clientConnection ) {
        
        // Create a transaction object 
        Transaction transaction = new Transaction( clientConnection ); 
        
        // Run transaction 
        transaction.test(); 
        
        
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
                }
            }
        }
    }
}
