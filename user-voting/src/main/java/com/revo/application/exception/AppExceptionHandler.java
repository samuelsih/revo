package com.revo.application.exception;

import com.revo.application.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.time.Instant;
import java.util.HashMap;

@ControllerAdvice
@Slf4j
public class AppExceptionHandler {
    @ExceptionHandler(ExpiredVotingTimeException.class)
    public ResponseEntity<Object> handleException(ExpiredVotingTimeException e) {
        return Response.make(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(VotingNotFoundException.class)
    public ResponseEntity<Object> handleException(VotingNotFoundException e) {
        return Response.make(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleException() {
        return Response.make(HttpStatus.BAD_REQUEST, "bad request");
    }

    @ExceptionHandler(UnknownUserException.class)
    public ResponseEntity<Object> handleException(UnknownUserException e) {
        return Response.make(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handle(MethodArgumentNotValidException e) {
        log.info(e.getMessage());

        HashMap<String, String> response = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            response.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        return Response.make(HttpStatus.BAD_REQUEST, "unprocessable request", response);
    }

    @ExceptionHandler({DynamoDbException.class, RuntimeException.class, VotingServerErrorException.class})
    public ResponseEntity<Object> handleException(Exception e, HttpServletRequest request) {
        log.error(String.format("INTERNAL ERROR: %s", e.getMessage()));
        log.info(request.getRequestURI() + "|" + Instant.now());
        return Response.make(HttpStatus.INTERNAL_SERVER_ERROR, "internal server error");
    }
}
