package com.revo.application.entity;

public class Status {
    public static String NOT_FOUND = "not found";
    public static String EXPIRED = "expired";
    public static String OK = "ok";
    public static String SERVER_ERROR = "error";

    public static String valueOf(String s) {
        if(s.equals(NOT_FOUND)) {
            return NOT_FOUND;
        }

        if(s.equals(EXPIRED)) {
            return EXPIRED;
        }

        if(s.equals(SERVER_ERROR)) {
            return SERVER_ERROR;
        }

        return OK;
    }
}
