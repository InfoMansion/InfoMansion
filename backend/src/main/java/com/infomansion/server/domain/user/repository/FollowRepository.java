package com.infomansion.server.domain.user.repository;

import com.infomansion.server.domain.user.domain.Follow;
import com.infomansion.server.domain.user.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFromUserIsAndToUserIs(User fromUser, User toUser);

    boolean existsByFromUserIdAndToUserIs(Long fromUserId, User toUser);

    @Query("SELECT f FROM Follow f join fetch f.toUser WHERE f.fromUser = :fromUser")
    List<Follow> findFollowingUserList(@Param("fromUser") User fromUser);

    @Query("SELECT f FROM Follow f join fetch f.toUser WHERE f.toUser = :toUser")
    List<Follow> findFollowerUserList(@Param("toUser") User toUser);

    @Query("SELECT f.toUser.id FROM Follow f WHERE f.fromUser = :fromUser")
    Slice<Long> findFollowingUserRecommend(@Param("fromUser") User fromUser, Pageable pageable);

    Long countByFromUserIs(User fromUser);

    Long countByToUserIs(User toUser);
}