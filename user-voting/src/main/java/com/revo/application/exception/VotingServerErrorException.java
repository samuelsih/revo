package com.revo.application.exception;

public class VotingServerErrorException extends Exception {
    public VotingServerErrorException() {
        super("internal server error");
    }
}
