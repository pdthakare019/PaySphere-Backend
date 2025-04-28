package com.example.Payroll.Exceptions;

public class InvalidDataException extends RuntimeException{
    public InvalidDataException(String message) {
        super(message);
    }
}
