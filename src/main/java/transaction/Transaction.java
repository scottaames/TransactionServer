/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction;

import java.util.ArrayList;

/**
 * An object that encapsulates all of the information about a transaction 
 * @author scott
 */
public class Transaction {
    
        // The idenfier for this transaction 
        int transID; 
        
        // The locks tha this transaction is holding 
        ArrayList<Lock> locks;
        
        // The string buffer that will be used to output all lock activity at the end
        StringBuffer log = new StringBuffer("");
        
        public Transaction( int transId ) {
            
            this.transID = transId; 
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
        
        public void log(String logString) {
            log.append("\n").append(logString);
            
            if (!TransactionServer.transactionView) {
                System.out.println("Transaction # " + this.getID() + ((this.getID() < 10) ? " " : "") + logString);
            }
        }
        
        public StringBuffer getLog() {
            return log;
        }

    }
