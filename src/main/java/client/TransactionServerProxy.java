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
import transaction.MessageTypes;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import transaction.Message;
import transaction.Transaction;
import transaction.TransactionServer;

import Logger.OutputLogger; 
import static transaction.MessageTypes.READ_REQUEST;

/**
 *
 * @author scott
 */
public class TransactionServerProxy implements MessageTypes {
    private Socket dbConnection = null;
    private ObjectOutputStream writeToNet = null;
    private ObjectInputStream readFromNet = null;
    private Integer transactionId = 0;
    
    String host = null;
    int port;
    
    OutputLogger logger = new OutputLogger( true ); 

    public TransactionServerProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }
    
    public int openTransaction() {
        
    }
    
    public void closeTransaction() {
        
    }
    
    public int read(int accountNumber) {
        Message readMessage = new Message(READ_REQUEST, accountNumber);
        Integer balance = null;

        try {
            writeToNet.writeObject(readMessage);
            balance = (Integer) readFromNet.readObject();
        }
        catch( Exception e){
            System.out.println("Server Proxy Read Error");
            e.printStackTrace();
        }

        return balance;
    }
    
    public int write(int accountNumber, int amount) {
        Message writeMessage = new Message(WRITE_REQUEST, accountNumber); // need amount
        Integer balance = null;

        try{
            writeToNet.writeObject(writeMessage);
            balance = (Integer) readFromNet.readObject();
        }
        catch( Exception e){
            System.out.println("Server Proxy Write Error");
            e.printStackTrace();
        }

        return balance;
    }
}
        /*
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
        } */ 
