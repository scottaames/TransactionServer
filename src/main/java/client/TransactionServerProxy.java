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

import Logger.OutputLogger; 

/**
 *
 * @author scott
 */
public class TransactionServerProxy extends Thread {
    Socket client = null;
    ObjectOutputStream writeToServer = null;
    ObjectInputStream readFromServer = null;

    Message message = null;
    Transaction transaction;
    int transactionId;
    int accountID;
    Object resultObject;
    OutputLogger logger = new OutputLogger( true ); 

    public TransactionServerProxy(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            // setting up object streams
            logger.print( "Connecting input and output to server" ); 
            readFromServer = new ObjectInputStream(client.getInputStream());
            writeToServer = new ObjectOutputStream(client.getOutputStream());    
            
            // Create our message 
            logger.print( "Creating message" ); 
            Message message;
            //Open Transactiona
            message = new Message(MessageTypes.OpenTransaction, null);
            
            // Send the message to the server 
            logger.print( "Sending message to server" ); 
            Transaction readObject;
            writeToServer.writeObject(message);
            
            // Obtain a response from the server 
            logger.print( "Getting reponse from server" ); 
            String messageFromServer = (String) readFromServer.readObject(); 
            
            System.out.println( messageFromServer ); 
            
            // Print the message to the user 
            

        } catch (IOException ex) {
            Logger.getLogger(TransactionServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        loop: while(true){
                
            // reading message
            try {
                message = (Message) readFromServer.readObject();
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
