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
    
    public int read(int accountId, Transaction transaction) {
        Account account = getAccount(accountId);
        (TransactionServer.lockManager).lock(account, transaction, READ_LOCK);
        return (getAccount(accountId)).getBalance();
    }
    
    public int write(int accountId, Transaction transaction, int balance) {
        Account account = getAccount(accountId);
        (TransactionServer.lockManager).lock(account, transaction, WRITE_LOCK);
    }
}
