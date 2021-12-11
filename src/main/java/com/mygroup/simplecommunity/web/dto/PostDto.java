package com.mygroup.simplecommunity.web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private String author;
    private String createdAt;
    private String modifiedAt;

    @Builder
    public PostDto(Long id, String title, String content, String author, String createdAt, String modifiedAt){
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
