package com.ibmmq.messageflow.repository;

import com.ibmmq.messageflow.model.Account;

public interface AccountRepository {

    public void findByNumber(int number);

    public void listAll();

    public void create(Account account);

    public void update(Account account);

    public void delete(int number);

    // Banking Methods
    public void withdraw(int number, double amount);

    public void deposit(int number, double amount);

    public void transfer(int sourceNumber, int destinationNumber, double amount);


}
