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
public interface LockTypes {
    public static final int EMPTY_LOCK = 11;
    public static final int READ_LOCK = 12;
    public static final int WRITE_LOCK = 13;
}
