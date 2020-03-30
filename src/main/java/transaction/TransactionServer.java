/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction;

import java.net.ServerSocket;
import java.io.IOException;

/**
 *
 * @author scott
 */
public class TransactionServer extends Thread {
    
    // The server port and ip that we want to use
    public final int SERVER_PORT = 8080; 
    public final String SERVER_IP = "127.0.0.1";
    public ServerSocket serverSocket;
    
    // The managers of the components of this project 
    public static TransactionManager transactionManager;
    public static AccountManager accountManager;
    public static LockManager lockManager;
    
    public static boolean transactionView; 
    
    /**
     * Constructor method 
     * 
     * @param accountBalances The balance that we want all the accounts created to start with 
     */
    public TransactionServer(int accountBalances, boolean transView ) {
        
        int initialBalance = accountBalances;
        int numberOfAccounts = 10;
        
        // Create an account manager object that all start with the same balance
        TransactionServer.accountManager = new AccountManager(numberOfAccounts, initialBalance);
        System.out.println("[TransactionServer.TransactionServer] Account Manager created");
        
        // Initialize our lock manager 
        boolean applyLocking = true;
        TransactionServer.lockManager = new LockManager( applyLocking );
        System.out.println("[TransactionServer.TransactionServer] Lock Manager created");
        
        // Initialize our transaction manager
        TransactionServer.transactionManager = new TransactionManager();
        transactionView = transView; 
        System.out.println("[TransactionServer.TransactionServer] Transaction Manager created");
        
        try {
            // Create a serversocket on our chosen port 
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("[TransactionServer.TransactionServer] ServerSocket created");
        } catch (IOException e) {
            System.out.println("[TransactionServer.TransactionServer] Could not create server socket");
            e.printStackTrace();
            System.exit(1);
        }
    }
    
     public void run() {
    // start serving clients in server loop ...
            
        while(true) {
            try {
                transactionManager.runTransaction( serverSocket.accept() );    

            } catch (IOException e) {
                System.out.println("[TransactionServer.run] Warning: Error accepting client");
                e.printStackTrace();
            }
        }
    }
}


