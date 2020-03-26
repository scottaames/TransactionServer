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
    private int lockType;
    
    public Lock(LockTypes lockType, Account account) {
        this.account = account;
        this.lockHolders = new ArrayList<Integer>();
        this.lockType = lockType;
    }
    
    public synchronized void acquire(int transactionID, int aLockType) {
        //log message goes here
        try {
            //log message goes here

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
        } else if ( lockHolders.size() == 1 && currentLockType == READ_LOCK && aLockType == WRITE_LOCK) {
            /* this transaction is a holder but needs a more exclusive lock */
            //log message goes here
            lockType = LockTypes.Write;
        }
        else
        {
            //setting a read lock on read lock it holds
            //setting write lock on write lock it holds
            //setting a read lock on a write lock it holds
            //log message goes here
        }
    }
    //releases locks given a transactionID on that transaction
    public synchronized void release(int transactionId) {
        lockHolders.remove(transactionId);
        if(lockHolders.isEmpty())
        {
            lockType = LockTypes.None;
        }
        notifyAll();
    }

    private boolean isConflict(int transactionId, int aLockType)
    {
        //no lock holders, no conflict
        if(lockHolders.isEmpty())
        {
            //log message goes here
            return false;
        }
        //this transcation has the lockholder, no conflict
        else if (lockHolders.size() == 1 && lockHolders.contains(transcationId))
        {
            //log message goes here
            return false;
        }
        else //conflict
        {
            int otherTransId;
            int index = 0;
            StringBuilder holders = new StringBuilder("");
            while(index < lockHolders.size())
            {
                otherTransId = lockHolders.get(index);
                holders.append(" ").append(otherTransId);
            }
            //log message goes here
            return true;
        }
    }

    public synchronized int getLockType()
    {
        return currentLockType;
    }

    public Account getAccount()
    {
        return account;
    }

    public static String getLockTypeString(int lockType)
    {
        String lockString = "Locktype not implemented";
        switch(lockType)
        {
            case READ_LOCK:

        }
    }

}
