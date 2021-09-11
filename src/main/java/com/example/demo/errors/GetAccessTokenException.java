package com.example.demo.errors;

public class GetAccessTokenException extends RuntimeException{

    public GetAccessTokenException(String message) {
        super(message);
    }

    public GetAccessTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
