package client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
    
    public TransactionClient(int numTransactions, int serverPort) {
       this.numTransactions = numTransactions;
       this.serverPort = serverPort;
    }
    
    @Override
    public void run() {
        for (int i = 0; i < numTransactions; i++) {
            new Thread() {
                @Override
                public void run() {
                    TransactionServerProxy transaction = new TransactionServerProxy("127.0.0.1", serverPort);
                    int transactionId = transaction.openTransaction();
                    System.out.println("transaction #" + transactionId + " started");
                    
                    int accountFrom - (int) Math.floor(Math.random() * numberAccounts);
                    int accountTo = (int) Math.floor(Math.random() * numberAccounts);
                    int amount = (int) Math.ceil(Math.random() * initialBalance);
                    int balance;
                    System.out.println("\ttransaction #" + transactionId + ", $" + amount + " " + accountFrom + "->" + accountTo);
                    
                    balance = transaction.read(accountFrom);
                    transaction.write(accountFrom, balance - amount);
                    
                    balance = transaction.read(accountTo);
                    transaction.write(accountTo, balance + amount);
                    
                    transaction.closeTransaction();
                    
                    System.out.println("transaction #" + transactionId + " finished");
                }
            }.start();
        }
    }
    
    /**
     * int transactionId;
            try { 
                
                // connect to application server
                Socket proxySocket = new Socket("127.0.0.1", serverPort);
                
                TransactionServerProxy proxy = new TransactionServerProxy( proxySocket ); 
                
                proxy.run(); 

                proxySocket.close(); 
                
                System.out.println( "Transaction: " + i ); 
                
            } catch (Exception e) {
                
            }
        }
        */
    
    public static void main(String[] args) {
        (new TransactionClient(20, 8080)).start();
    }
}
