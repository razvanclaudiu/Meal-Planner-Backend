package com.example.mealplannerbackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AwardNotFoundException extends RuntimeException {
    public AwardNotFoundException(String message) {
        super(message);
    }
}
