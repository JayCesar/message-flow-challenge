package com.ibmmq.messageflow.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckingAccount extends Account {
    private double limit;

    public CheckingAccount(int number, int agency, int type, String holder, double balance, double limit) {
        super(number, agency, type, holder, balance);
        this.limit = limit;
    }
}
