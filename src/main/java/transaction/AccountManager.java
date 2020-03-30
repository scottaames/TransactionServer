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
public class AccountManager implements LockTypes {
    
    ArrayList<Account> accounts;
    
    /**
     * Constructor that creates the accounts on the server 
     * 
     * @param numAccounts The number of accounts we want to create 
     * @param balance The balance we want all accounts to start with 
     */
    public AccountManager(int numAccounts, int balance){
        this.accounts = new ArrayList<Account>();
        for (int i = 0; i < numAccounts; i++) {
            accounts.add(new Account(i + 1, balance));
        }
    }
    
    public ArrayList<Account> getAccounts() {
        return accounts;
    }
    
    public Account getAccount(int accountId) {
        return accounts.get(accountId);
    }
    
    /**
     * Performs a read action for the desired account and locks the account with a read lock 
     * @param accountId The identifier for the account we want to read from 
     * @param transaction The transaction that will hold the read lock 
     * @return The balance of the account we want to access 
     */
    public int read(int accountId, Transaction transaction) {
        
        // Get the account that we want to read from 
        Account account = getAccount(accountId);
        
        // Set the lock and continue if we are allowed 
        (TransactionServer.lockManager).lock(account, transaction, READ_LOCK);
        
        // Return the balance of the account 
        return (account.getBalance());
    }
    
    /**
     * Performs a write action for the desired account and locks the account with a write lock 
     * @param accountId The identifier for the account we want to write to 
     * @param transaction The transaction that will hold the lock 
     * @param balance The balance that we want to set the account to 
     * @return 
     */
    public void write(int accountId, Transaction transaction, int balance) {
        Account account = getAccount(accountId);
        (TransactionServer.lockManager).lock(account, transaction, WRITE_LOCK);
        account.setBalance(balance);
    }
}
