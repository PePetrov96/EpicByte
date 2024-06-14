package com.project.EpicByte.exceptions;

public class CartItemNotFoundException extends RuntimeException {
    public CartItemNotFoundException() {
        super("CartItem not found.");
    }

    public CartItemNotFoundException(String message) {
        super(message);
    }

    public CartItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
