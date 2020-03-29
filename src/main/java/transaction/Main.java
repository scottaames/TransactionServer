/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction;

import client.TransactionClient;

/**
 *
 * @author scott
 */
public class Main {
    
    public static void main(String[] args) {
        
        TransactionClient client = new TransactionClient(20, 8080);
        TransactionServer server = new TransactionServer(10, true );
        
        new Thread() {
            @Override
            public void run() {
                server.run();
            }
        }.start();
                
        new Thread() {
            @Override
            public void run() {
                client.start();
            }
        }.start();
    }
}
