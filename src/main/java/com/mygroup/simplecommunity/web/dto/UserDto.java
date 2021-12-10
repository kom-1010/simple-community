package com.mygroup.simplecommunity.web.dto;

import com.mygroup.simplecommunity.domain.user.User;
import com.mygroup.simplecommunity.exception.MissingMandatoryPropertyException;
import lombok.Builder;
import lombok.Data;

import static com.mygroup.simplecommunity.exception.ErrorType.MISSING_MANDATORY_PROPERTY;

@Data
public class UserDto {
    private String email;
    private String password;
    private String name;
    private String phone;
    private String token;

    @Builder
    public UserDto(String email, String password, String name, String phone, String token){
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.token = token;
    }

    public User toEntity() {
        return User.builder().email(email).password(password).name(name).phone(phone).build();
    }
}
