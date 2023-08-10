package com.revo.application.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

public class Response {
    public static ResponseEntity<Object> make(HttpStatus status, String message, Object obj) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("code", status);
        result.put("msg", message);
        result.put("data", obj);

        return new ResponseEntity<>(result, status);
    }

    public static ResponseEntity<Object> make(HttpStatus status, String message) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("code", status);
        result.put("msg", message);

        return new ResponseEntity<>(result, status);
    }
}
