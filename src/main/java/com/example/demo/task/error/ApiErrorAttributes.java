package com.example.demo.task.error;

import java.util.Date;

public class ApiErrorAttributes {

    private Date timestamp;
    private String message;
    private String details;

    public ApiErrorAttributes(Date timestamp, String message, String details) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

}