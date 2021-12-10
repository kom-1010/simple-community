package com.mygroup.simplecommunity.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@AllArgsConstructor
public enum ErrorType {
    LOGIN_FAIL(BAD_REQUEST, "LOGIN_FAIL"),
    MISSING_MANDATORY_PROPERTY(BAD_REQUEST, "MISSING_MANDATORY_PROPERTY"),
    DUPLICATE_PROPERTY(BAD_REQUEST, "DUPLICATE_PROPERTY");

    private final HttpStatus httpStatus;
    private final String type;
}
