package com.mygroup.simplecommunity.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorType {
    UNAUTHORIZED_USER(UNAUTHORIZED, "UNAUTHORIZED_USER"),
    POST_NOT_FOUND(NOT_FOUND, "POST_NOT_FOUND"),
    INVALID_TOKEN(UNAUTHORIZED, "INVALID_TOKEN"),
    MISMATCH_PASSWORD(BAD_REQUEST, "MISMATCH_PASSWORD"),
    INVALID_PASSWORD(BAD_REQUEST, "INVALID_PASSWORD"),
    USER_NOT_FOUND(BAD_REQUEST, "USER_NOT_FOUND"),
    LOGIN_FAIL(BAD_REQUEST, "LOGIN_FAIL"),
    MISSING_MANDATORY_PROPERTY(BAD_REQUEST, "MISSING_MANDATORY_PROPERTY"),
    DUPLICATE_PROPERTY(BAD_REQUEST, "DUPLICATE_PROPERTY");

    private final HttpStatus httpStatus;
    private final String type;
}
