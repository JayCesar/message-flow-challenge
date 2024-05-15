package com.ibmmq.messageflow.repository;

import com.ibmmq.messageflow.model.Account;

public interface AccountRepository {

    public void findByNumber(int number);

    public void listAll();

    public void create(Account account);

    public void update(Account account);

    public void delete(int number);

    // Banking Methods
    public void withdraw(int number, float amount);

    public void deposit(int number, float amount);

    public void transfer(int sourceNumber, int destinationNumber, float amount);
}
