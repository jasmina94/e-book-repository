package com.ftn.exception;

public class MethodNotAllowedException extends RuntimeException {

    public MethodNotAllowedException() {
    }

    public MethodNotAllowedException(String message) {
        super(message);
    }
}
