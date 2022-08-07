package com.infomansion.server.domain.user.repository;

import com.infomansion.server.domain.user.domain.Follow;
import com.infomansion.server.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFromUserIsAndToUserIs(User fromUser, User toUser);

    boolean existsByFromUserIdAndToUserIs(Long fromUserId, User toUser);

    Long countByFromUserIs(User fromUser);

    Long countByToUserIs(User toUser);
}