package com.alibaba.sls.demo.tracelogdemo.exception;

public class CustomerNotExistException extends RuntimeException {
    public CustomerNotExistException(int id) {
        super("Custome[" + id + "] does not exist");
    }
}
