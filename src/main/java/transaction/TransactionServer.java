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

/**
 *
 * @author scott
 */
public class TransactionServer {
    
    public final int SERVER_PORT = 8080;
    public final String SERVER_IP = "127.0.0.1";
    public final int NUM_ACCOUNTS = 20;
    public ServerSocket serverSocket;
    public TransactionManager transactionManager;
    public AccountManager accountManager;
    public LockManager lockManager;
    public int numTransactionsToRun;
    
    
    public TransactionServer(int accountBalances) {
        accountManager = new AccountManager(NUM_ACCOUNTS, accountBalances);
        lockManager = new LockManager();
        transactionManager = new TransactionManager();
        
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
        } catch (Exception e) {
            Logger.getLogger(TransactionServer.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
     public void run() {
    // start serving clients in server loop ...

        try {
            while(true) {
                System.out.println("[Transaction Server] Waiting for connections on port "+ SERVER_PORT);
                //Accept incomming connections.
                Socket connectionToClient  = serverSocket.accept();
                System.out.println("[Transaction Server] A connection is established.");
                //Spin off new thread.
                (new TransactionServerProxy(connectionToClient)).start();
          }        

        } catch (Exception e) {
            System.err.println(e);
        }
    }
     
    public static void main(String[] args) {
        TransactionServer ts = new TransactionServer(10);
        ts.run();
    }
}


