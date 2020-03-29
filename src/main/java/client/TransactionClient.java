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
    int numberAccounts; 
    int initialBalance; 
    
    public TransactionClient(int numTransactions, int serverPort) {
       this.numTransactions = numTransactions;
       this.serverPort = serverPort;
    }
    
    @Override
    public void run() {
        
        numberAccounts = 10; 
        initialBalance = 10; 
        
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
                    
                    System.out.println( "reading" ); 
                    balance = proxy.read(accountFrom);
                    proxy.write(accountFrom, balance - amount);
                    
                    System.out.println( "Writing" ); 
                    balance = proxy.read(accountTo);
                    proxy.write(accountTo, balance + amount);
                    
                    System.out.println( "Closing" ); 
                    proxy.closeTransaction();
                    
                    System.out.println("transaction #" + transactionId + " finished");
                }
            }.start();
        }
    }
}
