package com.example.mealplannerbackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class QuantityNotFoundException extends RuntimeException{
    public QuantityNotFoundException(String message){
        super(message);
    }
}
