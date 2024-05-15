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
}
