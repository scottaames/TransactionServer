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
public class AccountManager {
    
    Account accounts[];
    
    public AccountManager(int numAccounts, int balance){
        this.accounts = new Account[numAccounts];
        for (int i = 0; i < numAccounts; i++) {
            this.accounts[i] = new Account(balance, i+ 1);
        }
    }
    
    public int getAccountBalance(int accountId, int transactionId) {
        return 0;
    }
    
    public void setAccountBalance(int accountID, int newBalance) {
        
    }
}
