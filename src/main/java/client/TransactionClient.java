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
    ReentrantLock outputLock = new ReentrantLock(); 
    
    public TransactionClient(int numTransactions, int serverPort) {
       this.numTransactions = numTransactions;
       this.serverPort = serverPort;
    }
    
    @Override
    public void run() {
        
        int numberAccounts = 10; 
        int initialBalance = 10; 
        
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
                    
                    balance = proxy.read(accountTo);
                    logger.log( "(" + transactionId + ")" + "balance " + accountTo + ": " + balance ); 
                    
                    proxy.write(accountTo, balance + amount);
                    logger.log( "(" + transactionId + ")" + "after balance " + accountTo + ": " + proxy.read( accountTo )); 
                    
                    proxy.closeTransaction();
                    
                    logger.log("transaction #" + transactionId + " finished");
                    
                    logger.printLog(); 
                }
            }.start();
        }
    }
    
    public class ClientLogger {
        
        ArrayList<String> logBuffer = new ArrayList<>();
        boolean logging; 
        ReentrantLock lock; 
        
        public ClientLogger( ReentrantLock lock ) {
            
            this.lock = lock; 
        }
        
        public void log( String stringLog ) { 
        
            logBuffer.add( stringLog ); 
        }

        public void printLog() {

            lock.lock(); 
            try { 
                for( String line: logBuffer ) {
                System.out.println( line );
                }
            } finally {
                System.out.println(); 
                lock.unlock(); 
                
            }
        }
    }
    
    
}
