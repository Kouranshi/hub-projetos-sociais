package com.example.demo.exception;

public class ApiException extends RuntimeException {
    public ApiException(String msg) {
        super(msg);
    }
}
