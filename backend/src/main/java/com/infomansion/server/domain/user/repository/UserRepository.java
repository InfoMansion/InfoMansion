package com.infomansion.server.domain.user.repository;

import com.infomansion.server.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findUsersByEmail(String email);
    List<User> findUsersByUsername(String username);
}
