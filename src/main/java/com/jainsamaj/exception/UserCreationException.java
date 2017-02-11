package com.jainsamaj.exception;

/**
 * Created by jaine03 on 29/01/17.
 */
public class UserCreationException extends RuntimeException {

    public UserCreationException(String message){
        super(message);
    }

    public UserCreationException(String message, Throwable cause){
        super(message, cause);
    }
}
