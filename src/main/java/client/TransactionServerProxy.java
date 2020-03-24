/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import transaction.Message.MessageTypes;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import transaction.Message;
import transaction.Transaction;
import transaction.TransactionServer;

/**
 *
 * @author scott
 */
public class TransactionServerProxy extends Thread {
    Socket client = null;
    ObjectOutputStream writeToClient = null;
    ObjectInputStream readFromClient = null;

    Message message = null;
    Transaction transaction;
    int transactionId;
    int accountID;
    Object resultObject;

    public TransactionServerProxy(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            // setting up object streams
            readFromClient = new ObjectInputStream(client.getInputStream());
            writeToClient = new ObjectOutputStream(client.getOutputStream());        

        } catch (IOException ex) {
            Logger.getLogger(TransactionServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        loop: while(true){
                
            // reading message
            try {
                message = (Message) readFromClient.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("[Transaction Server]: Message could not be read from object stream.");
                e.printStackTrace();
                System.exit(1);
            }
            
            switch (message.getType()) {
                case OpenTransaction:
                    System.out.println("opening transaction.");
            }
        }
    }
}
