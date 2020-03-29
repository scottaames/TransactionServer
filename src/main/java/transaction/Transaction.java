/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction;

import java.io.IOException;
import java.net.Socket;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

/**
 *
 * @author scott
 */
public class Transaction {
        int transID; 
        ArrayList<Lock> locks; 
        Socket client; 
        
        public Transaction( Socket client ) {
            
            this.client = client; 
        }
        
        public Transaction( int id ) {
            
            this.transID = id; 
            this.locks = new ArrayList(); 
        }
        
        public int getID() {
            
            return this.transID; 
        }
        
        public ArrayList<Lock> getLocks() {
            
            return locks; 
        }
        
        public void addLock( Lock lock ) {
            
            locks.add( lock ); 
        }
        
        public void write() {
            
            // Read the balance of the account associated with this transaction 
            
            // Add/subtract the amount of money that we want to change on this account 
            
        }
        
        public void read() {
            
            // Get the balance from the account associated with this transaction
            
            
            // Return the balance
        }
        
        public String getLog() {
            return "get log called"; 
        }

    }
