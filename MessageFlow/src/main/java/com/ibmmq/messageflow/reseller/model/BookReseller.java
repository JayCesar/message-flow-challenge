package com.ibmmq.messageflow.reseller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class BookReseller {
    private String id;
    private String name;
    private Double price;
}
