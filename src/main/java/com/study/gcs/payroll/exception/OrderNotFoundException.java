package com.study.gcs.payroll.exception;

public class OrderNotFoundException extends Exception {

    public OrderNotFoundException(Long id) {
        super("Order could not find: " + id);
    }
}
