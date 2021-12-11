package com.mygroup.simplecommunity.web;

import com.mygroup.simplecommunity.service.PostService;
import com.mygroup.simplecommunity.web.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@RestController
public class PostApiController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<?> create(@AuthenticationPrincipal String userId, @RequestBody PostDto requestDto){
        PostDto responseDto = postService.create(userId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
