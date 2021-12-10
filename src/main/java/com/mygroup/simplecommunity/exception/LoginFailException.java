package com.mygroup.simplecommunity.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.mygroup.simplecommunity.exception.ErrorType.LOGIN_FAIL;

@Getter
@NoArgsConstructor
public class LoginFailException extends CustomException{
    private static final ErrorType errorType = LOGIN_FAIL;

    public LoginFailException(String message){
        super(errorType, message);
    }

}
