/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction;

import java.util.ArrayList;

/**
 *
 * @author scott
 */
public class Lock {
    private Account account;
    private ArrayList<Integer> lockHolders;
    private LockTypes lockType;
    
    public enum LockTypes {
        Read,
        Write,
        None
    }
    
    public Lock(LockTypes lockType, Account account) {
        this.account = account;
        this.lockHolders = new ArrayList<Integer>();
        this.lockType = lockType;
    }
    
    public synchronized void acquire(int transactionID, LockTypes aLockType) {
        try {
            wait();
        } catch (InterruptedException e) {   
        }
        if (lockHolders.isEmpty()) { // no TIDs hol lock
            lockHolders.add(transactionID);
            lockType = aLockType;
        } else if (true /* anotehr transaction holds the lock, share it */) {
            if ( true /* this transaction not a holder */) {
                lockHolders.add(transactionID);
            }
        } else if ( true /* this transaction is a holder but needs a more exclusive lock */ ) {
            lockType = LockTypes.Write;
        }
    }
    
    public synchronized void release(int transactionId) {
        lockHolders.remove(transactionId);
        lockType = LockTypes.None;
    }
}
