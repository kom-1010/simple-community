package com.mygroup.simplecommunity.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.mygroup.simplecommunity.exception.ErrorType.USER_NOT_FOUND;

@Getter
@NoArgsConstructor
public class UserNotFoundException extends CustomException {
    private static final ErrorType errorType = USER_NOT_FOUND;

    public UserNotFoundException(String message) {
        super(errorType, message);
    }
}
