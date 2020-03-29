/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author scott
 */
public class Lock implements LockTypes {
    private Account account;
    private ArrayList<Transaction> lockHolders;
    private HashMap<Transaction, Object[]> lockRequestors;
    private int currentLockType;
    
    public Lock(int lockType, Account account) {
        this.account = account;
        this.lockHolders = new ArrayList<>();
        this.currentLockType = EMPTY_LOCK;
    }
    
    public synchronized void acquire(Transaction transaction, int newLockType) {
        //log message goes here
        //transaction.log("[Lock.acquire]     | try " + getLockTypeString(newLockType) + " on account #" + account.getAccountId());
        while (isConflict(transaction, newLockType))
        {
            try {
                //log message goes here

                wait();
            } catch (InterruptedException e) {   
            }
            if (lockHolders.isEmpty()) { // no TIDs hol lock
                lockHolders.add(transaction);
                currentLockType = newLockType;
            } else if (true /* anotehr transaction holds the lock, share it */) {
                if ( true /* this transaction not a holder */) {
                    lockHolders.add(transaction);
                }
            } else if (lockHolders.size() == 1 && currentLockType == READ_LOCK && currentLockType == WRITE_LOCK) {
                /* this transaction is a holder but needs a more exclusive lock */
                //log message goes here
                currentLockType = WRITE_LOCK;
            }
            else
            {
                //setting a read lock on read lock it holds
                //setting write lock on write lock it holds
                //setting a read lock on a write lock it holds
                //log message goes here
            }
        }
    }
    //releases locks given a transactionID on that transaction
    public synchronized void release(Transaction transaction) {
        lockHolders.remove(transaction);
        if(lockHolders.isEmpty())
        {
            currentLockType = EMPTY_LOCK;
        }
        notifyAll();
    }

    private boolean isConflict(Transaction transaction, int newLockType)
    {
        //no lock holders, no conflict
        if(lockHolders.isEmpty())
        {
            //log message goes here
            return false;
        }
        //this transcation has the lockholder, no conflict
        else if (lockHolders.size() == 1 && lockHolders.contains(transaction))
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
                otherTransId = lockHolders.get(index).getID();
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
                lockString = "Read Lock";
                break;
            case WRITE_LOCK:
                lockString = "Write Lock";
                break;
            case EMPTY_LOCK:
                lockString = "Empty Lock";
                break;
            
        }
        return lockString;
    }

}
