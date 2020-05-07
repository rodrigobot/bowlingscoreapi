package com.robot.bowlingscore.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GameCompleteException extends RuntimeException {
    public GameCompleteException(String message) {
        super(message);
    }
}
