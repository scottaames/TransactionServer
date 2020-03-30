package client;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock; 
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author scott
 */
public class TransactionClient extends Thread {
    
    int numTransactions;
    int serverPort;
    int numberAccounts; 
    int initialBalance; 
    ReentrantLock outputLock = new ReentrantLock(); 
    
    public TransactionClient(int numTransactions, int serverPort, int numOfAccounts, int initBalance ) {
       this.numTransactions = numTransactions;
       this.serverPort = serverPort;
       this.numberAccounts = numOfAccounts; 
       this.initialBalance = initBalance; 
    }
    
    @Override
    public void run() {
        
        for (int i = 0; i < numTransactions; i++) {
            new Thread() {
                @Override
                public void run() {
                    
                    ClientLogger logger = new ClientLogger( outputLock ); 
                    
                    TransactionServerProxy proxy = new TransactionServerProxy("127.0.0.1", serverPort);
                    int transactionId = proxy.openTransaction();
                    System.out.println("transaction #" + transactionId + " started");
                    
                    int accountFrom = (int) Math.floor(Math.random() * numberAccounts);
                    int accountTo = (int) Math.floor(Math.random() * numberAccounts);
                    int amount = (int) Math.ceil(Math.random() * initialBalance);
                    int balance;
                    
                    logger.log("\ttransaction #" + transactionId + ", $" + amount + " " + accountFrom + "->" + accountTo);
                    
                    // Read how much money is in the account that we want to transfer FROM 
                    balance = proxy.read(accountFrom);
                    logger.log( "(" + transactionId + ")" + "balance " + accountFrom + ": " + balance ); 
                    
                    // Write how much money it should have after the transfer 
                    proxy.write(accountFrom, balance - amount);
                    logger.log( "(" + transactionId + ")" + "after balance " + accountFrom + ": " + proxy.read( accountFrom )); 
                    
                    // Read how much money is in the account that we want to transfter TO 
                    balance = proxy.read(accountTo);
                    logger.log( "(" + transactionId + ")" + "balance " + accountTo + ": " + balance ); 
                    
                    // Write how much money it should have after the transfer 
                    proxy.write(accountTo, balance + amount);
                    logger.log( "(" + transactionId + ")" + "after balance " + accountTo + ": " + proxy.read( accountTo )); 
                    
                    // Close the transaction, now that we're done 
                    proxy.closeTransaction();
                    
                    logger.log("transaction #" + transactionId + " finished");
                    
                    // Print the transaction history of the two accounts and their new balances 
                    logger.printLog(); 
                }
            }.start();
        }
    }
    
    /**
     * Class that logs the activities of the transaction 
     */
    public class ClientLogger {
        
        // ArrayList of lines of String output 
        ArrayList<String> logBuffer = new ArrayList<>();
        
        // Boolean indicating whether or not we want to output the results at the end 
        boolean logging; 
        
        // Lock to prevent multiple logs being printed at once, keeping things together 
        ReentrantLock lock; 
        
        public ClientLogger( ReentrantLock lock ) {
            
            this.lock = lock; 
        }
        
        // Used to add a string line to our log buffer
        public void log( String stringLog ) { 
        
            logBuffer.add( stringLog ); 
        }

        // Prints all fo the log buffer at once
        public void printLog() {

            // Uses a lot of prevent logs being printed from interrupting each other 
            lock.lock(); 
            try { 
                
                // Print every line from the string buffer 
                for( String line: logBuffer ) {
                    System.out.println( line );
                }
            } finally {
                
                // Print an additional line for clarity 
                System.out.println(); 
                
                // Release the lock, letting another log be printed
                lock.unlock(); 
                
            }
        }
    }
    
    
}
