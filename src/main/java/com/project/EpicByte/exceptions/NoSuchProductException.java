package com.project.EpicByte.exceptions;

public class NoSuchProductException extends RuntimeException {
    public NoSuchProductException() {
        super("This product does not exist or was removed.");
    }

    public NoSuchProductException(String message) {
        super(message);
    }

    public NoSuchProductException(String message, Throwable cause) {
        super(message, cause);
    }
}
