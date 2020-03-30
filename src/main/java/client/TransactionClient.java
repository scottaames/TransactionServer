package client;

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
        
        int numberAccounts = 10; 
        int initialBalance = 10; 
        
        for (int i = 0; i < numTransactions; i++) {
            new Thread() {
                @Override
                public void run() {
                    TransactionServerProxy proxy = new TransactionServerProxy("127.0.0.1", serverPort);
                    int transactionId = proxy.openTransaction();
                    System.out.println("transaction #" + transactionId + " started");
                    
                    int accountFrom = (int) Math.floor(Math.random() * numberAccounts);
                    int accountTo = (int) Math.floor(Math.random() * numberAccounts);
                    int amount = (int) Math.ceil(Math.random() * initialBalance);
                    int balance;
                    System.out.println("\ttransaction #" + transactionId + ", $" + amount + " " + accountFrom + "->" + accountTo);
                    
                    balance = proxy.read(accountFrom);
                    System.out.println( "(" + transactionId + ")" + "balance " + accountFrom + ": " + balance ); 
                    proxy.write(accountFrom, balance - amount);
                    System.out.println( "(" + transactionId + ")" + "after balance " + accountFrom + ": " + proxy.read( accountFrom )); 
                    
                    balance = proxy.read(accountTo);
                    System.out.println( "(" + transactionId + ")" + "balance " + accountTo + ": " + balance ); 
                    proxy.write(accountTo, balance + amount);
                    System.out.println( "(" + transactionId + ")" + "after balance " + accountTo + ": " + proxy.read( accountTo )); 
                    
                    proxy.closeTransaction();
                    
                    System.out.println("transaction #" + transactionId + " finished");
                }
            }.start();
        }
    }
}
