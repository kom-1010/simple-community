package com.mygroup.simplecommunity.domain;

import com.mygroup.simplecommunity.domain.post.Post;
import com.mygroup.simplecommunity.domain.post.PostRepository;
import com.mygroup.simplecommunity.domain.user.User;
import com.mygroup.simplecommunity.domain.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PostRepositoryTests {
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


    @BeforeEach
    public void setUp(){
        author = userRepository.save(User.builder().email(email).password(encodedPassword).name(name).phone(phone).build());
    }

    @AfterEach
    public void tearDown(){
        userRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Test
    @Transactional
    public void create(){
        // given
        Post post = Post.builder().title(title).content(content).author(author).build();

        // when
        postRepository.save(post);

        // then
        Post savedPost = postRepository.findAll().get(0);
        assertThat(savedPost.getTitle()).isEqualTo(title);
        assertThat(savedPost.getContent()).isEqualTo(content);
        assertThat(savedPost.getAuthor().getName()).isEqualTo(name);
        assertThat(savedPost.getCreatedAt()).isBefore(LocalDateTime.now());
    }

    @Test
    @Transactional
    public void findAllDesc(){
        // given
        postRepository.save(Post.builder().title(title).content(content).author(author).build());

        // when
        Post post = postRepository.findAllDesc().get(0);

        // then
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getContent()).isEqualTo(content);
        assertThat(post.getAuthor().getName()).isEqualTo(name);
        assertThat(post.getCreatedAt()).isBefore(LocalDateTime.now());
    }

    @Test
    @Transactional
    public void findAllByTitleLikeDesc(){
        // given
        postRepository.save(Post.builder().title(title).content(content).author(author).build());

        // when
        Post post = postRepository.findAllByTitleLikeDesc("he").get(0);

        // then
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getContent()).isEqualTo(content);
        assertThat(post.getAuthor().getName()).isEqualTo(name);
        assertThat(post.getCreatedAt()).isBefore(LocalDateTime.now());
    }

    @Transactional
    @Test
    public void findAllByDescByAuthorLike(){
        // given
        postRepository.save(Post.builder().title(title).content(content).author(author).build());

        // when
        Post post = postRepository.findAllByAuthorLikeDesc("To").get(0);

        // then
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getContent()).isEqualTo(content);
        assertThat(post.getAuthor().getName()).isEqualTo(name);
        assertThat(post.getCreatedAt()).isBefore(LocalDateTime.now());
    }

    @Transactional
    @Test
    public void findById(){
        // given
        Long id = postRepository.save(Post.builder().title(title).content(content).author(author).build()).getId();

        // when
        Post post = postRepository.findById(id).get();

        // then
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getContent()).isEqualTo(content);
        assertThat(post.getAuthor().getName()).isEqualTo(name);
        assertThat(post.getCreatedAt()).isBefore(LocalDateTime.now());
    }

    @Transactional
    @Test
    public void modify(){
        // given
        String modifyTitle = "modify title";
        String modifyContent = "modify content";
        Post post = postRepository.save(Post.builder().title(title).content(content).author(author).build());
        post.modify(modifyTitle, modifyContent);

        // when
        postRepository.save(post);

        // then
        Post modifiedPost = postRepository.findAll().get(0);
        assertThat(modifiedPost.getTitle()).isEqualTo(modifyTitle);
        assertThat(modifiedPost.getContent()).isEqualTo(modifyContent);
        assertThat(modifiedPost.getModifiedAt()).isAfter(modifiedPost.getCreatedAt());
    }

    @Test
    public void remove(){
        // given
        Long id = postRepository.save(Post.builder().title(title).content(content).author(author).build()).getId();

        // when
        postRepository.deleteById(id);

        // then
        List<Post> posts = postRepository.findAll();
        assertThat(posts.size()).isEqualTo(0);
    }
}
