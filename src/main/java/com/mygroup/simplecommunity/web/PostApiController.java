package com.mygroup.simplecommunity.web;

import com.mygroup.simplecommunity.service.PostService;
import com.mygroup.simplecommunity.web.dto.PostDto;
import lombok.RequiredArgsConstructor;
import static org.springframework.http.HttpStatus.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@RestController
public class PostApiController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<?> create(@AuthenticationPrincipal String userId, @RequestBody PostDto requestDto){
        PostDto responseDto = postService.create(userId, requestDto);
        return ResponseEntity.status(CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<?> findAllDesc(){
        List<PostDto> responseDto = postService.findAllDesc();
        return ResponseEntity.status(OK).body(responseDto);
    }

    @GetMapping(params = "title")
    public ResponseEntity<?> findAllDescByTitle(@RequestParam("title") String keyword){
        List<PostDto> responseDto = postService.findAllDescByTitle(keyword);
        return ResponseEntity.status(OK).body(responseDto);
    }

    @GetMapping(params = "author")
    public ResponseEntity<?> findAllDescByAuthor(@RequestParam("author") String keyword){
        List<PostDto> responseDto = postService.findAllDescByAuthor(keyword);
        return ResponseEntity.status(OK).body(responseDto);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> findById(@PathVariable Long postId){
        PostDto responseDto = postService.findById(postId);
        return ResponseEntity.status(OK).body(responseDto);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<?> modify(@AuthenticationPrincipal String userId,
                                    @PathVariable Long postId,
                                    @RequestBody PostDto postDto){
        PostDto responseDto = postService.modify(userId, postId, postDto);
        return ResponseEntity.status(CREATED).body(responseDto);
    }
}
