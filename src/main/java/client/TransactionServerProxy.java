/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import transaction.MessageTypes;
import java.net.Socket;
import transaction.Message;

/**
 * Acts as an API for the server on the client's side
 * @author scott
 */
public class TransactionServerProxy implements MessageTypes {
    private Socket dbConnection = null;
    private ObjectOutputStream writeToNet = null;
    private ObjectInputStream readFromNet = null;
    private Socket connectionToServer = null; 
    
    String host = null;
    int port;

    /**
     * Constructor method 
     * @param host The host IP address of the server we want to connect to 
     * @param port The port of the server we want to connect to 
     */
    public TransactionServerProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }
    
    /**
     * Opens a transaction to the server 
     * @return The id number of the transaction 
     */
    public int openTransaction() {
        
        // Create an open connection message to send to the server 
        Message openMessage = new Message(OPEN_TRANSACTION, null );
        
        try {
            
            // Create a two-way connection to the server
            connectionToServer = new Socket( host, port ); 
            writeToNet = new ObjectOutputStream( connectionToServer.getOutputStream() ); 
            readFromNet = new ObjectInputStream( connectionToServer.getInputStream() ); 
             
            // Send our openTransaction message to the server 
            writeToNet.writeObject(openMessage);
            
            // Read the respose from the server, the id of teh transaction created 
            return ( int )readFromNet.readObject(); 
            
        } catch(IOException | ClassNotFoundException e ) {
            System.out.println("Server Proxy Open Transaction error.");
            e.printStackTrace();
            System.exit(1);
            return 0; 
        }
        
    }
    
    /**
     * Closes the transaction connection to the server 
     */
    public void closeTransaction() {
        
        // Create a closeTransaction message to send to the server 
        Message closeMessage = new Message(CLOSE_TRANSACTION, null);
        
        try {
            
            // Send the message to the server to close the connection 
            writeToNet.writeObject(closeMessage);
        } catch (IOException e) {
            System.out.println("Server Proxy Close Transaction error.");
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Reads the balance of a chosen account 
     * @param accountNumber The identifier for the account that we want tor read from 
     * @return The balance of that account 
     */
    public int read(int accountNumber) {
        
        // Create a read message that contains the identifier for the account we want to read from
        Message readMessage = new Message(READ_REQUEST, accountNumber);
        
        // Initialize our balance variable 
        Integer balance = null;

        try {
            
            // Send our read message to the server 
            writeToNet.writeObject(readMessage);
            
            // Save the balance of the account that we wanted to access 
            balance = (Integer) readFromNet.readObject();
        }
        catch( Exception e){
            System.out.println("Server Proxy Read Error");
            e.printStackTrace();
            System.exit(1);
        }

        // Return the balance to the transactionClient 
        return balance;
    }
    
    /**
     * Sends a write function to a chosen account and sets its balance 
     * @param accountNumber The id of the account we want to access 
     * @param amount The value we want to replace its balance with
     */
    public void write(int accountNumber, int amount) {
        
        // Create the write message we want to send to the server
        Message writeMessage = new Message(WRITE_REQUEST, accountNumber + "," + amount); // need amount

        try{
            
            // Send our write message to the server 
            writeToNet.writeObject(writeMessage);
        }
        catch( Exception e){
            System.out.println("Server Proxy Write Error");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
