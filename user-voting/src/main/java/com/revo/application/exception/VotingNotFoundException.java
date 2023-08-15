package com.revo.application.exception;

public class VotingNotFoundException extends Exception {
    public VotingNotFoundException() {
        super("voting not found");
    }
}
