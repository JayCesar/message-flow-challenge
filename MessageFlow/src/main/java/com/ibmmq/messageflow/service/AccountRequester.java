package com.ibmmq.messageflow.service;

import com.ibmmq.messageflow.controller.AccountController;
import com.ibmmq.messageflow.model.CheckingAccount;
import com.ibmmq.messageflow.model.SavingsAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AccountRequester {


    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AccountRequester.class, args);
    }

    public static void createAccounts() {
        CheckingAccount ca1 = new CheckingAccount(1, 123, 1, "João da Silva", 1000f, 100.0);

//        CheckingAccount ca2 = new CheckingAccount(accounts.generateNumber(), 124, 1, "Maria da Silva", 2000f, 100.0);
//        accounts.create(ca2);
//
//        SavingsAccount sc1 = new SavingsAccount(accounts.generateNumber(), 125, 2, "Mariana dos Santos", 4000f, 12);
//        accounts.create(sc1);
//
//        SavingsAccount sc2 = new SavingsAccount(accounts.generateNumber(), 125, 2, "Juliana Ramos", 8000f, 15);
//        accounts.create(sc2);
    }

}



