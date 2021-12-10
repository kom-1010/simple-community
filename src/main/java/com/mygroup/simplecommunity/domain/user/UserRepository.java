package com.mygroup.simplecommunity.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmailAndPhone(String email, String phone);

    Optional<User> findByNameAndPhone(String name, String phone);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
