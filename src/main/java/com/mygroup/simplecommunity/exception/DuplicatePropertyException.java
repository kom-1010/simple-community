package com.mygroup.simplecommunity.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.mygroup.simplecommunity.exception.ErrorType.DUPLICATE_PROPERTY;

@Getter
@NoArgsConstructor
public class DuplicatePropertyException extends CustomException {
    private static final ErrorType errorType = DUPLICATE_PROPERTY;

    public DuplicatePropertyException(String message){
        super(errorType, message);
    }

    public DuplicatePropertyException(ErrorType errorType, String message){
        super(errorType, message);
    }
}
