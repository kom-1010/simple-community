package com.mygroup.simplecommunity.exception;

import static com.mygroup.simplecommunity.exception.ErrorType.INVALID_PASSWORD;

public class InvalidPasswordException extends CustomException {
    private static final ErrorType errorType = INVALID_PASSWORD;
    public InvalidPasswordException(String message) {
        super(errorType, message);
    }
}
