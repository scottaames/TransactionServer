package client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import transaction.Message;
import transaction.Message.MessageTypes;
import transaction.Transaction;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author scott
 */
public class TransactionClient extends Thread {
    
    int numTransactions;
    int serverPort;
    
    public TransactionClient(int numTransactions, int serverPort) {
       this.numTransactions = numTransactions;
       this.serverPort = serverPort;
    }
    
    @Override
    public void run() {
        for (int i = 0; i < numTransactions; i++) {
            int transactionId;
            try { 
                // connect to application server
                Socket serverProxy = new Socket("127.0.0.1", serverPort);

                //Setup Object Streams
                ObjectOutputStream writeToServer = new ObjectOutputStream(serverProxy.getOutputStream());
                ObjectInputStream readFromServer = new ObjectInputStream(serverProxy.getInputStream());

                Message message;
                
                //Open Transactiona
                message = new Message(MessageTypes.OpenTransaction, null);
                Transaction readObject;
                writeToServer.writeObject(message);

                transactionId = (Integer) readFromServer.readObject();
                System.out.println("[Client][Transaction " + transactionId + "]: Transaction opened");
            } catch (Exception e) {
                
            }
        }
    }
    
    public static void main(String[] args) {
        for (int i = 0; i < 1; i ++) {
            (new TransactionClient(20, 8080)).start();
        }
    }
}
