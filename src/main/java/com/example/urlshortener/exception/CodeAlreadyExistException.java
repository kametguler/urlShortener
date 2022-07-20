package com.example.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CodeAlreadyExistException extends RuntimeException{
    public CodeAlreadyExistException(String message){
        super(message);
    }
}
