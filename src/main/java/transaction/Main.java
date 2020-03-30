/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction;

import client.TransactionClient;
import java.util.Scanner; 

/**
 *
 * @author scott
 */
public class Main {
    
    public static void main(String[] args) {
        
        // Initialize our scanner for input from the terminal 
        Scanner input = new Scanner( System.in ); 
        
        // Get the number of accounts we want to use from the user 
        System.out.println( "How many accounts would you like to use?: " ); 
        int numberOfAccounts = input.nextInt(); 
        
        // Get the initial balance that each account will have from the user 
        System.out.println( "What is the balance you want initially for all accounts?: " ); 
        int initialBalance = input.nextInt(); 
        
        // Get the number of transactions we want done
        System.out.println( "What number of transactions do you want?: " ); 
        int numOfTrans = input.nextInt(); 

        // Initialize our client and server objects 
        TransactionClient client = new TransactionClient( numOfTrans, 8080, numberOfAccounts, initialBalance );
        TransactionServer server = new TransactionServer( initialBalance, true, numberOfAccounts );

        // Run the client and server on different threads. 
        server.start();
        client.start();
        
    }
}
