package com.udemy.projeto.exception;

public class DbException extends RuntimeException{

    public DbException(String msg) {
        super(msg);
    }

}
