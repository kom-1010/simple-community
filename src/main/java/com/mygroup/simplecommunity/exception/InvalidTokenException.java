package com.mygroup.simplecommunity.exception;

import static com.mygroup.simplecommunity.exception.ErrorType.INVALID_TOKEN;

public class InvalidTokenException extends CustomException {
    private static final ErrorType errorType = INVALID_TOKEN;

    public InvalidTokenException(String message) {
        super(errorType, message);
    }
}
