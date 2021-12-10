package com.mygroup.simplecommunity.domain.user;

import static com.mygroup.simplecommunity.exception.ErrorType.*;
import com.mygroup.simplecommunity.exception.MissingMandatoryPropertyException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String phone;

    @Builder
    public User(String id, String email, String password, String name, String phone){
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

    public void modifyPassword(String password) {
        this.password = password;
    }

    public void modifyProfile(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public void encodePassword() {
        this.password = new BCryptPasswordEncoder().encode(this.password);
    }

    public boolean matchPassword(String password) {
        return new BCryptPasswordEncoder().matches(password, this.password);
    }
}
