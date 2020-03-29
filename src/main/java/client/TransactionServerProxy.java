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
    private static int transactionId = 0;
    private Socket connectionToServer = null; 
    
    String host = null;
    int port;
    
    OutputLogger logger = new OutputLogger( true ); 

    public TransactionServerProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }
    
    public int openTransaction() {
        transactionId++;
        Message openMessage = new Message(OPEN_TRANSACTION, transactionId);
        
        try {
            
            // Create a connection to the server
            connectionToServer = new Socket( host, port ); 
            writeToNet = new ObjectOutputStream( connectionToServer.getOutputStream() ); 
            readFromNet = new ObjectInputStream( connectionToServer.getInputStream() ); 
            
            System.out.println( "sending message" ); 
            writeToNet.writeObject(openMessage);
            System.out.println( "message sent" );
        } catch(IOException e) {
            System.out.println("Server Proxy Open Transaction error.");
            e.printStackTrace();
            System.exit(1);
        }
        return transactionId;
    }
    
    public void closeTransaction() {
        Message closeMessage = new Message(CLOSE_TRANSACTION, null);
        
        try {
            writeToNet.writeObject(closeMessage);
        } catch (IOException e) {
            System.out.println("Server Proxy Close Transaction error.");
            e.printStackTrace();
            System.exit(1);
        }
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
            System.exit(1);
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
            System.exit(1);
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
