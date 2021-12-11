package com.mygroup.simplecommunity.exception;

import static com.mygroup.simplecommunity.exception.ErrorType.UNAUTHORIZED_USER;

public class UnauthorizedUserException extends CustomException {
    private static final ErrorType errorType = UNAUTHORIZED_USER;
    public UnauthorizedUserException(String message) {
        super(errorType, message);
    }
}
