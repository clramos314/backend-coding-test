package com.example.demo.task.error;

public class SearchTaskNoResultException extends RuntimeException{

    private static final long serialVersionUID = 2L;

    public SearchTaskNoResultException() {
        super("Searching of tasks had no results");
    }
}
