/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction;

import java.io.Serializable;

/**
 *
 * @author scott
 */
public class Transaction implements Serializable {
        Account account;
        int transactionId;
        int amount;

        public Transaction(Account account, int amount, int transactionId) {
            this.account = account;
            this.amount = amount;
            this.transactionId = transactionId;
        }

        public Account getAccount() {
            return account;
        }

        public int getAmount() {
            return amount;
        }
        
        public int getTransactionId() {
            return transactionId;
        }
    }
