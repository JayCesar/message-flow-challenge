package com.ibmmq.messageflow.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public abstract class Account {
    private int number;
    private int agency;
    private int type;
    private String holder;
    private double balance;

    public boolean withdraw(double value) {

        if(this.getBalance() < value) {
            System.out.println("\n Saldo Insuficiente!");
            return false;
        }

        this.setBalance(this.getBalance() - value);
        return true;
    }

    public void visualize(){
        System.out.println(this.getType());
    }

    public void deposit(double amount) {

        this.setBalance(this.getBalance() + amount);

    }
}
