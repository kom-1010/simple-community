package com.mygroup.simplecommunity.service;

import com.mygroup.simplecommunity.domain.post.Post;
import com.mygroup.simplecommunity.domain.post.PostRepository;
import com.mygroup.simplecommunity.domain.user.User;
import com.mygroup.simplecommunity.domain.user.UserRepository;
import com.mygroup.simplecommunity.exception.MissingMandatoryPropertyException;
import com.mygroup.simplecommunity.exception.PostNotFoundException;
import com.mygroup.simplecommunity.exception.UserNotFoundException;
import com.mygroup.simplecommunity.web.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public PostDto create(String userId, PostDto requestDto){
        if(requestDto.getTitle() == null)
            throw new MissingMandatoryPropertyException("Title is mandatory");
        if(requestDto.getContent() == null)
            throw new MissingMandatoryPropertyException("Content is mandatory");

        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User cannot be found"));

        Post post = Post.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .author(user)
                .build();

        postRepository.save(post);
        return PostDto.builder().build();
    }

    public List<PostDto> findAllDesc() {
        List<Post> posts = postRepository.findAllDesc();
        return posts.stream().map(post -> PostDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .author(post.getAuthor().getName())
                        .createdAt(post.getCreatedAt().toString())
                        .modifiedAt(post.getModifiedAt().toString())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public List<PostDto> findAllDescByTitle(String keyword) {
        List<Post> posts = postRepository.findAllByTitleLikeDesc(keyword);
        return posts.stream().map(post -> PostDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .author(post.getAuthor().getName())
                        .createdAt(post.getCreatedAt().toString())
                        .modifiedAt(post.getModifiedAt().toString())
                        .build())
                .collect(Collectors.toList());
    }

    public List<PostDto> findAllDescByAuthor(String keyword) {
        List<Post> posts = postRepository.findAllByAuthorLikeDesc(keyword);
        return posts.stream().map(post -> PostDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .author(post.getAuthor().getName())
                        .createdAt(post.getCreatedAt().toString())
                        .modifiedAt(post.getModifiedAt().toString())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public PostDto findById(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new PostNotFoundException("Post cannot found"));

        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getAuthor().getName())
                .createdAt(post.getCreatedAt().toString())
                .modifiedAt(post.getModifiedAt().toString())
                .build();
    }
}
