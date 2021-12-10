package com.mygroup.simplecommunity.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResponseDto {
    private String type;
    private String message;

    @Builder
    public ErrorResponseDto(String type, String message){
        this.type = type;
        this.message = message;
    }
}
