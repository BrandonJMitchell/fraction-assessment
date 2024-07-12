package com.baseball.fractionassessment.exceptions;

public class PlayerDoesNotExistException extends Exception {
    public PlayerDoesNotExistException() {
        super();
    }

    public PlayerDoesNotExistException(String message) {
        super(message);
    }

    public PlayerDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlayerDoesNotExistException(Throwable cause) {
        super(cause);
    }
}
