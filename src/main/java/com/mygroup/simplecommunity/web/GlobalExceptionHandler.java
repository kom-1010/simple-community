package com.mygroup.simplecommunity.web;

import com.mygroup.simplecommunity.exception.CustomException;
import com.mygroup.simplecommunity.exception.DuplicatePropertyException;
import com.mygroup.simplecommunity.exception.MissingMandatoryPropertyException;
import com.mygroup.simplecommunity.web.dto.ErrorResponseDto;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> customExceptionHandle(CustomException e){
        return ResponseEntity.status(e.getErrorType().getHttpStatus())
                .body(ErrorResponseDto.builder()
                        .type(e.getErrorType().getType())
                        .message(e.getMessage())
                        .build());
    }
//    @ExceptionHandler(MissingMandatoryPropertyException.class)
//    public ResponseEntity<?> missingMandatoryProperty(MissingMandatoryPropertyException e){
//        return ResponseEntity.status(e.getErrorType().getHttpStatus()).body(ErrorResponseDto.builder()
//                .type(e.getErrorType().getType())
//                .message(e.getMessage())
//                .build());
//    }
//
//    @ExceptionHandler(DuplicatePropertyException.class)
//    public ResponseEntity<?> duplicateProperty(DuplicatePropertyException e){
//        return ResponseEntity.status(e.getErrorType().getHttpStatus()).body(ErrorResponseDto.builder()
//                .type(e.getErrorType().getType())
//                .message(e.getMessage())
//                .build());
//    }
}
