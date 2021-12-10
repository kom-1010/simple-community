package com.mygroup.simplecommunity.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygroup.simplecommunity.domain.user.User;
import com.mygroup.simplecommunity.domain.user.UserRepository;
import com.mygroup.simplecommunity.web.dto.UserDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static com.mygroup.simplecommunity.exception.ErrorType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserApiControllerTests {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;

    private String email = "abc123@abcde.com";
    private String password = "abc12345";
    private String name = "Teddy";
    private String phone = "010-1234-5678";

    @AfterEach
    public void tearDown(){
        userRepository.deleteAll();
    }

    @Test
    public void signup() throws Exception {
        // given
        UserDto userDto = UserDto.builder().email(email).password(password).name(name).phone(phone).build();
        String url = "/api/v1/users/signup";

        // when
        mvc.perform(post(url)
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isCreated());

        // then
        User user = userRepository.findAll().get(0);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getPhone()).isEqualTo(phone);
        assertThat(user.getPassword()).isNotEqualTo(password);
    }

    @Test
    public void signup_fail_by_missing_mandatory_property() throws Exception {
        // given
        UserDto userDto = UserDto.builder().password(password).name(name).phone(phone).build();
        String url = "/api/v1/users/signup";

        // when
        mvc.perform(post(url)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value(MISSING_MANDATORY_PROPERTY.getType()))
                .andExpect(jsonPath("$.message").value("Email is mandatory"));
    }

    @Test
    public void signup_fail_by_duplicate_email() throws Exception {
        // given
        userRepository.save(User.builder().email(email).password("123").name("mike").phone("000").build());
        UserDto userDto = UserDto.builder().email(email).password(password).name(name).phone(phone).build();
        String url = "/api/v1/users/signup";

        // when
        mvc.perform(post(url)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value(DUPLICATE_PROPERTY.getType()))
                .andExpect(jsonPath("$.message").value("Email is duplicated"));
    }

    @Test
    public void login() throws Exception {
        // given
        userRepository.save(User.builder()
                .email(email)
                .password(new BCryptPasswordEncoder().encode(password))
                .name(name)
                .phone(phone)
                .build());
        UserDto userDto = UserDto.builder().email(email).password(password).build();
        String url = "/api/v1/users/login";

        // when
        mvc.perform(post(url)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    public void login_fail_by_missing_mandatory_property() throws Exception {
        // given
        userRepository.save(User.builder()
                .email(email)
                .password(new BCryptPasswordEncoder().encode(password))
                .name(name)
                .phone(phone)
                .build());
        UserDto userDto = UserDto.builder().password(password).build();
        String url = "/api/v1/users/login";

        // when
        mvc.perform(post(url)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value(MISSING_MANDATORY_PROPERTY.getType()))
                .andExpect(jsonPath("$.message").value("Email is mandatory"));
    }

    @Test
    public void login_fail_by_invalid_email() throws Exception {
        // given
        userRepository.save(User.builder()
                .email(email)
                .password(new BCryptPasswordEncoder().encode(password))
                .name(name)
                .phone(phone)
                .build());
        UserDto userDto = UserDto.builder().email("invalid").password(password).build();
        String url = "/api/v1/users/login";

        // when
        mvc.perform(post(url)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value(LOGIN_FAIL.getType()))
                .andExpect(jsonPath("$.message").value("Email is invalid"));
    }

    @Test
    public void login_fail_by_invalid_password() throws Exception {
        // given
        userRepository.save(User.builder()
                .email(email)
                .password(new BCryptPasswordEncoder().encode(password))
                .name(name)
                .phone(phone)
                .build());
        UserDto userDto = UserDto.builder().email(email).password("invalid").build();
        String url = "/api/v1/users/login";

        // when
        mvc.perform(post(url)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value(LOGIN_FAIL.getType()))
                .andExpect(jsonPath("$.message").value("Password is invalid"));
    }
}
