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

        TransactionClient client = new TransactionClient(3, 8080);
        TransactionServer server = new TransactionServer(10, true );

        server.start();
        client.start();
    }
}
