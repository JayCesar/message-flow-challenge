package com.ibmmq.messageflow.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SavingsAccount extends Account {

    private int birthday;

    public SavingsAccount(int number, int agency, int type, String holder, double balance, int birthday) {
        super(number, agency, type, holder, balance);
        this.birthday = birthday;
    }
}
