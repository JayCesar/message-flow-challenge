package com.ibmmq.messageflow.controller;

import com.ibmmq.messageflow.model.Account;
import com.ibmmq.messageflow.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountController {

    private List<Account> accountList = new ArrayList<>();
    int number;

    public void findByNumber(int number) {
        var account = findInCollection(number);

        if (account != null)
            account.visualize();
        else
            System.out.println("\nA Conta número: " + number + " não foi encontrada!");
    }

//    @Override
//    public void listAll() {
//        for (var account : accountList) {
//            account.visualize();
//        }
//    }
//
//    @Override
//    public void create(Account account) {
//        accountList.add(account);
//        System.out.println("\nA Conta número: " + account.getNumber() + " foi criada com sucesso!");
//    }
//
//    @Override
//    public void update(Account account) {
//        var buscaConta = findInCollection(account.getNumber());
//
//        if (buscaConta != null) {
//            accountList.set(accountList.indexOf(buscaConta), account);
//            System.out.println("\nA Conta numero: " + account.getNumber() + " foi atualizada com sucesso!");
//        } else
//            System.out.println("\nA Conta numero: " + account.getNumber() + " não foi encontrada!");
//    }
//
//    @Override
//    public void delete(int number) {
//        var conta = findInCollection(number);
//
//        if (conta != null) {
//            if (accountList.remove(conta) == true)
//                System.out.println("\nA Conta numero: " + number + " foi deletada com sucesso!");
//        } else
//            System.out.println("\nA Conta numero: " + number + " não foi encontrada!");
//    }
//
//    @Override
//    public void withdraw(int number, double amount) {
//        var account = findInCollection(number);
//
//        if (account != null) {
//
//            if (account.withdraw(amount) == true)
//                System.out.println("\nO Saque na Conta numero: " + number + " foi efetuado com sucesso!");
//
//        } else
//            System.out.println("\nA Conta numero: " + number + " não foi encontrada!");
//    }
//
//    @Override
//    public void deposit(int number, double amount) {
//        var account = findInCollection(number);
//
//        if (account != null) {
//            account.deposit(amount);
//            System.out.println("\nO Depósito na Conta numero: " + number + " foi efetuado com sucesso!");
//        } else
//            System.out.println("\nA Conta numero: " + number + " não foi encontrada ou a Conta destino não é uma Conta Corrente!");
//    }
//
//    @Override
//    public void transfer(int sourceNumber, int destinationNumber, double amount) {
//        var sourceAccount = findInCollection(sourceNumber);
//        var targetAccount = findInCollection(destinationNumber);
//
//        if (sourceAccount != null && targetAccount != null) {
//
//            if (sourceAccount.withdraw(amount) == true) {
//                targetAccount.deposit(amount);
//                System.out.println("\nA Transferência foi efetuado com sucesso!");
//            }
//        } else
//            System.out.println("\nA Conta de Origem e/ou Destino não foram encontradas!");
//    }

    // class methods
    public Account findInCollection(int number) {
        for (var account : accountList)
            if (account.getNumber() == number)
                return account;
        return null;
    }

//    public int generateNumber() {
//        return ++number;
//    }

}
