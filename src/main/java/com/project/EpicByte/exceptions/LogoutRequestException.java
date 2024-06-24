package com.project.EpicByte.exceptions;

public class LogoutRequestException extends RuntimeException {
    public LogoutRequestException() {
        super("Logout is required.");
    }

    public LogoutRequestException(String message) {
        super(message);
    }

    public LogoutRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}

