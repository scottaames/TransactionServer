/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction;

import java.io.Serializable;
import java.net.Socket;

/**
 *
 * @author scott
 */
public class TransactionManager {
    
    
    
    public TransactionManager() {
        
        
    }
    
    public void addTransaction( Socket clientConnection ) {
        
        // Create a transaction object 
        Transaction transaction = new Transaction( clientConnection ); 
        
        // Run transaction 
        transaction.test(); 
    }
}
