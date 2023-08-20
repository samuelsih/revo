package com.revo.application.exception;

public class UnknownUserException extends Exception {
    public UnknownUserException() {
        super("unknown user");
    }
}
