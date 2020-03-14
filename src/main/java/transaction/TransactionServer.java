/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction;

import java.net.ServerSocket;

/**
 *
 * @author scott
 */
public class TransactionServer {
    
    public final int SERVER_PORT = 8080;
    public final String SERVER_IP = "127.0.0.1";
    public ServerSocket serverSocket;
    public TransactionManager transactionManager;
    public AccountManager accountManager;
    public LockManager lockManager;
    public int numTransactionsToRun;
    
    
    public TransactionServer(int numTransactionsToRun) {
        
    }
}
