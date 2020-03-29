package client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import transaction.TransactionServer;

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
    
    public TransactionClient(int numTransactions, int serverPort) {
       this.numTransactions = numTransactions;
       this.serverPort = serverPort;
    }
    
    @Override
    public void run() {
        
        numberAccounts = TransactionServer.NUM_ACCOUNTS; 
        initialBalance = TransactionServer.initialBalance; 
        
        for (int i = 0; i < numTransactions; i++) {
            new Thread() {
                @Override
                public void run() {
                    TransactionServerProxy transaction = new TransactionServerProxy("127.0.0.1", serverPort);
                    int transactionId = transaction.openTransaction();
                    System.out.println("transaction #" + transactionId + " started");
                    
                    int accountFrom = (int) Math.floor(Math.random() * numberAccounts);
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
}
