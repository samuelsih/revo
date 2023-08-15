package com.revo.application.exception;

public class ExpiredVotingTimeException extends Exception {
    public ExpiredVotingTimeException() {
        super("voting time has expired");
    }
}
