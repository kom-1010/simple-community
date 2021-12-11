package com.mygroup.simplecommunity.web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostDto {
    private String id;
    private String title;
    private String content;

    @Builder
    public PostDto(String title, String content){
        this.title = title;
        this.content = content;
    }
}
