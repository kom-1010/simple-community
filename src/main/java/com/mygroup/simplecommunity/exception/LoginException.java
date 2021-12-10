package com.mygroup.simplecommunity.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.mygroup.simplecommunity.exception.ErrorType.LOGIN_FAIL;

@Getter
@NoArgsConstructor
public class LoginException extends CustomException{
    private static final ErrorType errorType = LOGIN_FAIL;

    public LoginException(String message){
        super(errorType, message);
    }

}
