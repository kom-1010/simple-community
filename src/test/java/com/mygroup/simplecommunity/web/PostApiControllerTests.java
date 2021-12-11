package com.mygroup.simplecommunity.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygroup.simplecommunity.domain.post.Post;
import com.mygroup.simplecommunity.domain.post.PostRepository;
import com.mygroup.simplecommunity.domain.user.User;
import com.mygroup.simplecommunity.domain.user.UserRepository;
import com.mygroup.simplecommunity.security.TokenProvider;
import com.mygroup.simplecommunity.web.dto.PostDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.mygroup.simplecommunity.exception.ErrorType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class PostApiControllerTests {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    private String title = "hello";
    private String content = "hello! My name is Tom.";
    private User author;
    private String email = "abc123@abcde.com";
    private String password = "abc12345";
    private String encodedPassword = new BCryptPasswordEncoder().encode(password);
    private String name = "Tom";
    private String phone = "010-1234-5678";
    private String token;


    @BeforeEach
    public void setUp(){
        author = userRepository.save(User.builder().email(email).password(encodedPassword).name(name).phone(phone).build());
        token = tokenProvider.create(author.getId());
    }

    @AfterEach
    public void tearDown(){
        userRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Transactional
    @Test
    public void create() throws Exception {
        // given
        PostDto postDto = PostDto.builder().title(title).content(content).build();
        String url = "/api/v1/posts";

        // when
        mvc.perform(post(url).header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(postDto)))
                .andExpect(status().isCreated());

        // then
        Post post = postRepository.findAll().get(0);
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getContent()).isEqualTo(content);
        assertThat(post.getAuthor().getName()).isEqualTo(name);
        assertThat(post.getCreatedAt()).isBefore(LocalDateTime.now());
    }

    @Test
    public void fail_create_by_missing_mandatory_property() throws Exception {
        // given
        PostDto postDto = PostDto.builder().content(content).build();
        String url = "/api/v1/posts";

        // when
        mvc.perform(post(url).header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(postDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value(MISSING_MANDATORY_PROPERTY.getType()));
    }

    @Test
    public void fail_create_by_user_not_found() throws Exception {
        // given
        PostDto postDto = PostDto.builder().title(title).content(content).build();
        String userInvalidToken = tokenProvider.create("invalid");
        String url = "/api/v1/posts";

        // when
        mvc.perform(post(url).header("Authorization", "Bearer " + userInvalidToken)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(postDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value(USER_NOT_FOUND.getType()));
    }

    @Test
    public void fail_create_by_invalid_token() throws Exception {
        // given
        PostDto postDto = PostDto.builder().title(title).content(content).build();
        String invalidToken = "invalid";
        String url = "/api/v1/posts";

        // when
        mvc.perform(post(url).header("Authorization", "Bearer " + invalidToken)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(postDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.type").value(INVALID_TOKEN.getType()));
    }

    @Transactional
    @Test
    public void find_all_desc() throws Exception {
        // given
        postRepository.save(Post.builder().title(title).content(content).author(author).build());
        String url = "/api/v1/posts";

        // when, then
        mvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(title))
                .andExpect(jsonPath("$[0].content").value(content))
                .andExpect(jsonPath("$[0].author").value(name))
                .andExpect(jsonPath("$[0].createdAt").exists());
    }

    @Transactional
    @Test
    public void find_all_desc_by_title() throws Exception {
        // given
        postRepository.save(Post.builder().title(title).content(content).author(author).build());
        String url = "/api/v1/posts?title=" + "he";

        // when, then
        mvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(title))
                .andExpect(jsonPath("$[0].content").value(content))
                .andExpect(jsonPath("$[0].createdAt").exists())
                .andExpect(jsonPath("$[0].author").value(name));
    }

    @Transactional
    @Test
    public void find_all_desc_by_author() throws Exception {
        // given
        postRepository.save(Post.builder().title(title).content(content).author(author).build());
        String url = "/api/v1/posts?author=" + "To";

        // when, then
        mvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(title))
                .andExpect(jsonPath("$[0].content").value(content))
                .andExpect(jsonPath("$[0].author").value(name))
                .andExpect(jsonPath("$[0].createdAt").exists());
    }

    @Transactional
    @Test
    public void find_by_id() throws Exception {
        // given
        Long id = postRepository.save(Post.builder().title(title).content(content).author(author).build()).getId();
        String url = "/api/v1/posts/" + id;

        // when, then
        mvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.author").value(name));
    }

    @Transactional
    @Test
    public void find_by_id_by_not_found_post() throws Exception {
        // given
        Long id = postRepository.save(Post.builder().title(title).content(content).author(author).build()).getId();
        String url = "/api/v1/posts/" + "-1";

        // when, then
        mvc.perform(get(url))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value(POST_NOT_FOUND.getType()));
    }
}
