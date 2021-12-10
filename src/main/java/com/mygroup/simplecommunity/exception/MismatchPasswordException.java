package com.mygroup.simplecommunity.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.mygroup.simplecommunity.exception.ErrorType.MISMATCH_PASSWORD;

@Getter
@NoArgsConstructor
public class MismatchPasswordException extends CustomException {
    private static final ErrorType errorType = MISMATCH_PASSWORD;
    public MismatchPasswordException(String message) {
        super(errorType, message);
    }
}
