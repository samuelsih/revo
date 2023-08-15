package com.revo.application.response;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

public record Response() {
    public static ResponseEntity<Object> make(
            @NonNull HttpStatus status,
            @NonNull String message
    ) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("code", status.value());
        result.put("msg", message);

        return new ResponseEntity<>(result, status);
    }

    public static ResponseEntity<Object> make(
            @NonNull HttpStatus status,
            @NonNull String message,
            @NonNull Object data
    ) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("code", status.value());
        result.put("msg", message);
        result.put("data", data);

        return new ResponseEntity<>(result, status);
    }
}
