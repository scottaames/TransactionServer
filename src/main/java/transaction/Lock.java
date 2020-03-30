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
        transaction.log("[Lock.acquire            | try " +
                getLockTypeString(newLockType) + "on account #" + account.getAccountId());
        while (isConflict(transaction, newLockType))
        {
            try {
                transaction.log("[lock.acquire            | -> wait to set " +
                                getLockTypeString(newLockType) + "on account #" + account.getAccountId());
                addLockRequestor(transaction, newLockType);
                wait();
            } catch (InterruptedException e) {   
            }
            if (lockHolders.isEmpty())
            { // no TIDs hol lock
                lockHolders.add(transaction);
                currentLockType = newLockType;
                transaction.addLock(this);
                transaction.log("[Lock.acquire]     | lock set to " + getLockTypeString(currentLockType) + " on account # " + account.getAccountId());
            }
            else if (!lockHolders.contains(transaction) /* anotehr transaction holds the lock, share it */)
            {
                lockHolders.add(transaction);
                Transaction otherTransaction;
                StringBuilder logString = new StringBuilder("[Lock.acquire          | share " +
                                                            getLockTypeString(currentLockType) + "on account # " +
                                                            account.getAccountId());
                int index = 0;
                while(index < lockHolders.size())
                {
                    otherTransaction = lockHolders.get(index);
                    logString.append(" ").append(otherTransaction.getID());
                }
                transaction.log(logString.toString());
            }
            else if (lockHolders.size() == 1 && currentLockType == READ_LOCK && newLockType == WRITE_LOCK)
            {
                /* this transaction is a holder but needs a more exclusive lock */
                transaction.log("[Lock.acquire]            | promote " +
                        getLockTypeString(currentLockType) + " to " + getLockTypeString(newLockType) + " on account # " + account.getAccountId());
                currentLockType = newLockType;
            }
            else
            {
                //setting a read lock on read lock it holds
                //setting write lock on write lock it holds
                //setting a read lock on a write lock it holds
                transaction.log("[Lock.isConflict]            |current lock " +
                        getLockTypeString(currentLockType) + "on account "+ account.getAccountId());
            }
        }
    }
    //releases locks given a transactionID on that transaction
    public synchronized void release(Transaction transaction) {
        lockHolders.remove(transaction);
        if(lockHolders.isEmpty())
        {
            currentLockType = EMPTY_LOCK;
            
            if (lockRequestors.isEmpty())
            {
                // this lock is not used anymore, so delete it
            }
        }
        notifyAll();
    }

    private boolean isConflict(Transaction transaction, int newLockType)
    {
        //no lock holders, no conflict
        if(lockHolders.isEmpty())
        {
            transaction.log("[Lock.isConflict]            |current lock " +
                            getLockTypeString(currentLockType) + "on account "+ account.getAccountId());
            return false;
        }
        //this transaction has the lockholder, no conflict
        else if (lockHolders.size() == 1 && lockHolders.contains(transaction))
        {
            transaction.log("[Lock.isConflict]            |current lock " +
                            getLockTypeString(currentLockType) + "on account "+ account.getAccountId());
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
                index++;
            }
            transaction.log("[Lock.isConflict]            |current lock " +
                    getLockTypeString(currentLockType) + "held by transaction "+ holders + " on account #" + account.getAccountId());
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

    private void addLockRequestor(Transaction trans, int lockType)
    {
        lockRequestors.put(trans, lockHolders);
    }

    private void removeLockRequestor(Transaction trans)
    {
        if(lockRequestors.get(trans) != null)
        {
            lockRequestors.remove(trans);
        }
    }

}
