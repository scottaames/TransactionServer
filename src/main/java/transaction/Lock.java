/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction;

import java.util.List;

/**
 *
 * @author scott
 */
public class Lock {
    private Account account;
    private List<Integer> lockHolders;
    private LockTypes lockType;
    
    public enum LockTypes {
        Read,
        Write,
        None
    }
    
    public Lock(LockTypes lockType, Account account) {
        this.account = account;
        this.lockHolders = new List<Integer>();
        this.lockType = lockType;
    }
    
    public synchronized void acquire(int transactionID, LockTypes lockType) {
        try {
            wait();
        } catch (InterruptedException e) {
            if (lockHolders.isEmpty()) { // no TIDs hol lock
                lockHolders.add(transactionID);
            }
        }
    }
    
    public synchronized void release(int transactionId) {
        lockHolders.remove(transactionId);
    }
}
