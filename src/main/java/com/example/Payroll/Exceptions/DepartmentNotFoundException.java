package com.example.Payroll.Exceptions;

public class DepartmentNotFoundException extends RuntimeException{
    public DepartmentNotFoundException(String message){
        super(message);
    }
}
