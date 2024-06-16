package com.project.EpicByte.exceptions;

public class UsernameIsEmptyException extends RuntimeException {
    public UsernameIsEmptyException() {
        super("User does not exit.");
    }

    public UsernameIsEmptyException(String message) {
        super(message);
    }

    public UsernameIsEmptyException(String message, Throwable cause) {
        super(message, cause);
    }
}

