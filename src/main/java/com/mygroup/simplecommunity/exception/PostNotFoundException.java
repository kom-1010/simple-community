package com.mygroup.simplecommunity.exception;

import static com.mygroup.simplecommunity.exception.ErrorType.POST_NOT_FOUND;

public class PostNotFoundException extends CustomException {
    private static final ErrorType errorType = POST_NOT_FOUND;

    public PostNotFoundException(String message) {
        super(errorType, message);
    }
}
