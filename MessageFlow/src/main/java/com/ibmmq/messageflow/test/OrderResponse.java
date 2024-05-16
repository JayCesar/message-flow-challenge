package com.ibmmq.messageflow.test;

import java.io.Serializable;

public class OrderResponse implements Serializable {
    private String message;
    private int status;

    public OrderResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}