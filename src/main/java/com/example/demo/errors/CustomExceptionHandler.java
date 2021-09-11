package com.example.demo.errors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import static com.example.demo.utils.ApiUtils.ApiResult;
import static com.example.demo.utils.ApiUtils.error;

@ControllerAdvice
public class CustomExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private ResponseEntity<ApiResult<?>> newResponse(Throwable throwable, HttpStatus status) {
        return newResponse(throwable.getMessage(), status);
    }

    private ResponseEntity<ApiResult<?>> newResponse(String message, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(error(message, status), headers, status);
    }

    @ExceptionHandler({
            NotFoundException.class,
            HttpClientErrorException.class,
            GetAccessTokenException.class
    })
    public ResponseEntity<?> handleNotFoundException(Exception e) {
        return newResponse(e, HttpStatus.NOT_FOUND);
    }
    public ResponseEntity<?> handleHttpClientErrorException(Exception e) {
        return newResponse(e, HttpStatus.BAD_REQUEST);
    }
    public ResponseEntity<?> handleGetAccessTokenException(Exception e) {
        return newResponse(e, HttpStatus.BAD_REQUEST);
    }

}
