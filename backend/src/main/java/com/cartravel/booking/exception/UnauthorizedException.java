package com.cartravel.booking.exception;
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) { super(message); }
}