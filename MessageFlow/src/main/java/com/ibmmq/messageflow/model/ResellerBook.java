package com.ibmmq.messageflow.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ResellerBook {
    private String id;
    private String name;
    private Double price;
}
