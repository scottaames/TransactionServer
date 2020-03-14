/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author scott
 */
public class TransactionServerProxy {
    private ServerSocket clientServerSocket;
    public int clientPort;
    public int serverPort;
    
    public TransactionServerProxy(int clientPort, int serverPort) throws IOException {
        this.clientServerSocket = new ServerSocket(clientPort);
        this.clientPort = clientPort;
        this.serverPort = serverPort;
    }
    
    public void runServer() {
        while (true) {
            Socket client = null, server = null;
            try {
                //write to server
            } catch(IOException e) {
                
            }
        }
    }
}
