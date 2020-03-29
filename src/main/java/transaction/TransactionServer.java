/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction;

import client.TransactionServerProxy;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import Logger.OutputLogger; 

/**
 *
 * @author scott
 */
public class TransactionServer {
    
    // The server port that we want to use
    public final int SERVER_PORT = 8080;
    
    // The IP address of local host that we want all of our nodes to run on 
    public final String SERVER_IP = "127.0.0.1";
    
    public static final int NUM_ACCOUNTS = 20;
    public static int initialBalance; 
    public ServerSocket serverSocket;
    
    // The managers of the components of this project 
    public static TransactionManager transactionManager;
    public static AccountManager accountManager;
    public static LockManager lockManager;
 
    private OutputLogger logger = new OutputLogger( true ); 
    
    public int numTransactionsToRun;
    public static boolean transactionView; 
    
    /**
     * Constructor method 
     * 
     * @param accountBalances The balance that we want all the accounts created to start with 
     */
    public TransactionServer(int accountBalances, boolean transView ) {
        
        initialBalance = accountBalances; 
        
        // Create an account manager object that all start with the same balance
        accountManager = new AccountManager(NUM_ACCOUNTS, accountBalances);
        
        // Initialize our lock manager 
        lockManager = new LockManager( true );
        
        // Initialize our transaction manager
        transactionManager = new TransactionManager();
        
        transactionView = transView; 
        
        try {
            
            // Create a serversocket on our chosen port 
            serverSocket = new ServerSocket(SERVER_PORT);
        } catch (Exception e) {
            Logger.getLogger(TransactionServer.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
     public void run() {
    // start serving clients in server loop ...

        try {
            
            while(true) {
                
                // Let the user know that the server is waiting 
                System.out.println( "[Transaction Server] Waiting for connections on port "+ SERVER_PORT);
                
                //Accept incomming connections.
                Socket connectionToClient  = serverSocket.accept();
                
                System.out.println("[Transaction Server] A connection is established.");
                
                //Spin off new thread.
                transactionManager.runTransaction( connectionToClient ); 
            }    

        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
    }
}


