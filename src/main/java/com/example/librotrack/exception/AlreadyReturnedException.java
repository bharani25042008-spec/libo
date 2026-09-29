package com.example.librotrack.exception;

public class AlreadyReturnedException extends RuntimeException {

    public AlreadyReturnedException(String message) {
        super(message);
    }
}
