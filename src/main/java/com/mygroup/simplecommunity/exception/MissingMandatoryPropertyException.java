package com.mygroup.simplecommunity.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.mygroup.simplecommunity.exception.ErrorType.MISSING_MANDATORY_PROPERTY;

@Getter
@NoArgsConstructor
public class MissingMandatoryPropertyException extends CustomException {
    private static final ErrorType errorType = MISSING_MANDATORY_PROPERTY;

    public MissingMandatoryPropertyException(String message) {
        super(errorType, message);
    }
    public MissingMandatoryPropertyException(ErrorType errorType, String message){
        super(errorType, message);
    }
}
