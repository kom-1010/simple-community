package com.mygroup.simplecommunity.domain;

import com.mygroup.simplecommunity.domain.user.User;
import com.mygroup.simplecommunity.domain.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserRepositoryTests {
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
    public void save(){
        // given
        User user = User.builder().email(email).password(password).name(name).phone(phone).build();

        // when
        userRepository.save(user);

        // then
        User savedUser = userRepository.findAll().get(0);
        assertThat(savedUser.getEmail()).isEqualTo(email);
        assertThat(savedUser.getPassword()).isEqualTo(password);
        assertThat(savedUser.getName()).isEqualTo(name);
        assertThat(savedUser.getPhone()).isEqualTo(phone);
    }

    @Test
    public void findByNameAndPhone(){
        // given
        userRepository.save(User.builder().email(email).password(password).name(name).phone(phone).build());

        // when
        User user = userRepository.findByNameAndPhone(name, phone).get();

        // then
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getPhone()).isEqualTo(phone);
    }

    @Test
    public void findByEmailAndPhone(){
        // given
        userRepository.save(User.builder().email(email).password(password).name(name).phone(phone).build());

        // when
        User user = userRepository.findByEmailAndPhone(email, phone).get();

        // then
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getPhone()).isEqualTo(phone);
    }

    @Test
    public void modify(){
        // given
        User user = userRepository.save(User.builder().email(email).password(password).name(name).phone(phone).build());

        String modifiedPassword = "def12345";
        String modifiedName = "Tom";
        String modifiedPhone = "000-9876-5432";

        user.modifyPassword(modifiedPassword);
        user.modifyProfile(modifiedName, modifiedPhone);

        // when
        userRepository.save(user);

        // then
        User modifiedUser = userRepository.findAll().get(0);
        assertThat(modifiedUser.getPassword()).isEqualTo(modifiedPassword);
        assertThat(modifiedUser.getName()).isEqualTo(modifiedName);
        assertThat(modifiedUser.getPhone()).isEqualTo(modifiedPhone);
    }

    @Test
    public void remove(){
        // given
        String id = userRepository.save(User.builder().email(email).password(password).name(name).phone(phone).build()).getId();

        // when
        userRepository.deleteById(id);

        // then
        List<User> users = userRepository.findAll();
        assertThat(users.size()).isEqualTo(0);
    }
}
