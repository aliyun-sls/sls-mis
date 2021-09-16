package com.aliyun.sls.demo.tracewithlog.backend.exception;

public class CustomerNotExistException extends RuntimeException {
    public CustomerNotExistException(int customerId) {

    }
}
