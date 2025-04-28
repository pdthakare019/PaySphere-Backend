package com.example.Payroll.Exceptions;
import java.util.*;
public class ErrorResponse {
    private int statusCode;
    private Date timestamp;
    private String message;
    private String details;

    // Non-argument constructor
    public ErrorResponse() {
    }

    public ErrorResponse(int statusCode, Date timestamp, String message, String details) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}