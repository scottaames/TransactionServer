/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction;

import java.io.IOException;
import java.net.Socket;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

/**
 *
 * @author scott
 */
public class Transaction {
    int transID; 
    ArrayList<Lock> locks = null; 
    StringBuffer log = new StringBuffer();

    public Transaction( int id ) {

        this.transID = id; 
        this.locks = new ArrayList(); 
    }

    public int getID() {

        return this.transID; 
    }

    public ArrayList<Lock> getLocks() {

        return locks; 
    }

    public void addLock( Lock lock ) {

        locks.add( lock ); 
    }

    public void test() {

        try {
        // Print to the server that the Transaction is working
        System.out.println( "transaction working" ); 

        // Send a message through to the socket to ensure connection 
        ObjectOutputStream writeToClient = new ObjectOutputStream( this.client.getOutputStream());
        writeToClient.writeObject( "Message from Transaction" ); 

        } catch (IOException ex) {
            Logger.getLogger(TransactionServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void write() {

        // Read the balance of the account associated with this transaction 

        // Add/subtract the amount of money that we want to change on this account 

    }

    public void read() {

        // Get the balance from the account associated with this transaction

        // Return the balance
    }

}
