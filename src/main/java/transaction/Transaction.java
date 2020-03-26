/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author scott
 */
public class Transaction implements Serializable {
        Account account;
        int transactionId;
        int amount;
        Socket client; 

        public Transaction(Account account, int amount, int transactionId, Socket client) {
            this.account = account;
            this.amount = amount;
            this.transactionId = transactionId;
            this.client = client; 
        }
        
        public Transaction( Socket client ) {
            
            this.client = client; 
        }
        
        public void test() {
            
            try {
            // Print to the server that the Transaction is working
            System.out.println( "transaction working" ); 
            
            // Send a message through to the socket to ensure connection 
            ObjectOutputStream writeToClient = new ObjectOutputStream( this.client.getOutputStream());
            writeToClient.writeObject( "Message from Transaction" ); 
            
            } catch (IOException ex) {
                Logger.getLogger(TransactionServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        public void write() {
            
            // 
            
        }
        
        public void read() {
            
            // Transmit the account client 
        }

        public Account getAccount() {
            return account;
        }

        public int getAmount() {
            return amount;
        }
        
        public int getTransactionID() {
            return transactionId;
        }
    }
