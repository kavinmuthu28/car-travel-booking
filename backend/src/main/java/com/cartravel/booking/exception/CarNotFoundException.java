package com.cartravel.booking.exception;
public class CarNotFoundException extends RuntimeException {
    public CarNotFoundException(String message) { super(message); }
}