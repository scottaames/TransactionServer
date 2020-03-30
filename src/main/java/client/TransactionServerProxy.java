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
             
            writeToNet.writeObject(openMessage);
            return ( int )readFromNet.readObject(); 
            
        } catch(IOException | ClassNotFoundException e ) {
            System.out.println("Server Proxy Open Transaction error.");
            e.printStackTrace();
            System.exit(1);
            return 0; 
        }
        
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
    
    public void write(int accountNumber, int amount) {
        Message writeMessage = new Message(WRITE_REQUEST, accountNumber, amount); // need amount
        Integer balance = null;

        try{
            writeToNet.writeObject(writeMessage);
            //balance = (Integer) readFromNet.readObject();
        }
        catch( Exception e){
            System.out.println("Server Proxy Write Error");
            e.printStackTrace();
            System.exit(1);
        }

        //return balance;
    }
}
