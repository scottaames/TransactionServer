/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction;

import java.util.HashMap;
import transaction.Lock.LockTypes;

/**
 *
 * @author scott
 */
public class LockManager {
    
    private HashMap<Lock, Account> theLocks;
    
    public void setLock(Account account, int transactionId, LockTypes lockType) {
        
    }
    
    public synchronized void unlock(int transactionId) {
        
    }
}
