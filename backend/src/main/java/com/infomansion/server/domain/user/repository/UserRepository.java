package com.infomansion.server.domain.user.repository;

import com.infomansion.server.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
