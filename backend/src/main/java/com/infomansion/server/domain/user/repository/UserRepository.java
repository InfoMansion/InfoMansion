package com.infomansion.server.domain.user.repository;

import com.infomansion.server.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
