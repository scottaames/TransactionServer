/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction;

import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author scott
 */
public class LockManager implements LockTypes {
    
    private HashMap<Account, Lock> locks;
    
    private boolean applyLocking;
    
    public LockManager(boolean applyLocking) {
        this.locks = new HashMap<>();
        this.applyLocking = applyLocking;
    }
    
    public void lock(Account account, Transaction transaction, int lockType) {
        // return, if we don't do locking
        if (!applyLocking) return;
        
        // get the lock that is attached to this account
        Lock lock;
        synchronized (this)
        {
            lock = locks.get(account);
            
            if (lock == null) 
            {
                // there is no lock attached to this account, create one
                lock = new Lock( lockType, account );
                locks.put(account, lock);
                
                //transaction.log("[LockManager.setLock]      | lock created, account #" + account.getAccountId());
            }
        }
        lock.acquire(transaction, lockType);
    }
    
    public synchronized void unLock(Transaction transaction) {
        if (!applyLocking) return;
        
        Iterator<Lock> lockIterator = transaction.getLocks().listIterator();
        Lock currentLock;
        while (lockIterator.hasNext())
        {
            currentLock = lockIterator.next();
            //transaction.log("[LockManager.unlock]       | releae " + Lock.getLockTypeString(currentLock.getLockType()) + ", account #" + currentLock.getAccount().getAccountID());
        }
    }
}
