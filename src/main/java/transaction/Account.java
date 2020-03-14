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
    private int accountId;
    private int lockTransactionId;
    private int locktype;
    private int numReadLocks;

    public Account(int balance, int accountId) {
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
