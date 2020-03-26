/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logger;

import transaction.Transaction; 
import transaction.Message; 

/**
 *
 * @author joshu
 */
public class OutputLogger {
    
    public enum MessageTypes {
        OpenTransaction,
        CloseTransaction,
        Read,
        Write
    }
    
    boolean logflag; 
    
    public OutputLogger( boolean activated ) {
        
        this.logflag = activated; 
    }
    
    public void log(Transaction trans, MessageTypes messageType )
    {
        if(logflag)
        {
            switch(messageType)
            {
                case OpenTransaction:
                {
                    System.out.println("Transaction Opened with "+ trans.getTransactionID() +
                                        "Account #: "+ trans.getAccount().getAccountID());
                    break;
                }
                case CloseTransaction:
                {
                    System.out.println("Transaction Closed with "+ trans.getTransactionID() +
                                        "\tAccount #: "+ trans.getAccount().getAccountID());
                    break;
                }
                case Read:
                {
                    System.out.println("Reading Transaction #: "+ trans.getTransactionID() +
                                        "\t Account #: "+ trans.getAccount().getAccountID() +
                                        "\t Balance: $"+ trans.getAccount().getBalance());
                    break;
                }
                case Write:
                {
                    System.out.println("Writing Transaction #: "+ trans.getTransactionID() +
                                        "\t Account #: "+ trans.getAccount().getAccountID());
                    break;
                }
            }
        }
    }
    
    public void print( String output ) {
     
        System.out.println( output ); 
    }
}
