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
public class Account {
    private int balance;
    private final int accountId;

    public Account(int accountId, int balance) {
        this.balance = balance;
        this.accountId = accountId;
    }

    public int getBalance() {
        return this.balance;
    }
    
    public void setBalance(int newBalance) {
        this.balance = newBalance;
    }

    public int getAccountId() {
        return this.accountId;
    } 
}
