package com.alibaba.sls.demo.tracelogdemo.protocol;

public class Request<T> {
    private boolean success;
    private String message;
    private T result;

    public Request(boolean success, String message, T result) {
        this.success = success;
        this.message = message;
        this.result = result;
    }

    public Request(boolean success, String message) {
        this.success = success;
        this.message = message;
    }


    public Request() {
    }

    public static <T> Request<T> ofSuccess(T result) {
        return new Request<T>(true, "Success", result);
    }

    public static <T> Request ofFailure(String message) {
        return new Request(false, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
