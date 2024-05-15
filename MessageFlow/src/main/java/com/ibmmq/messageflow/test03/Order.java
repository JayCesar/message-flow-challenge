package com.ibmmq.messageflow.test03;

public class Order {
    String id;
    String details;
    String confirmation;

    Order(String id, String details) {
        this.id = id;
        this.details = details;
    }

    void setConfirmation(String confirmation) {
        this.confirmation = confirmation;
    }
}
