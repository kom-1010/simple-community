package com.mygroup.simplecommunity.service;

import com.mygroup.simplecommunity.domain.post.Post;
import com.mygroup.simplecommunity.domain.post.PostRepository;
import com.mygroup.simplecommunity.domain.user.User;
import com.mygroup.simplecommunity.domain.user.UserRepository;
import com.mygroup.simplecommunity.exception.MissingMandatoryPropertyException;
import com.mygroup.simplecommunity.exception.UserNotFoundException;
import com.mygroup.simplecommunity.web.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
