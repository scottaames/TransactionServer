/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction;

/**
 *
 * @author scott
 */
public interface MessageTypes {
    
    public static final int OPEN_TRANSACTION = 1;
    public static final int CLOSE_TRANSACTION = 2;
    
    public static final int READ_REQUEST = 3;
    public static final int WRITE_REQUEST = 4;
    
}
