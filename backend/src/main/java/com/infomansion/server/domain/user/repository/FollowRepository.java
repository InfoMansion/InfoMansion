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

    @Query(value = "SELECT u.username, u.profile_image, " +
            "CASE " +
            "WHEN f2.from_user_id IS NULL THEN 0 " +
            "    ELSE 1 " +
            "END " +
            "FROM follow f1 INNER JOIN user u ON f1.to_user_id = u.user_id " +
            "LEFT JOIN follow f2 " +
            "ON f2.from_user_id = :loginUserId AND f2.to_user_id = f1.to_user_id " +
            "WHERE f1.from_user_id = :fromUserId", nativeQuery = true)
    List<Object[]> findFollowingUserList(@Param("loginUserId") Long loginUserId, @Param("fromUserId") Long fromUserId);

    @Query(value = "SELECT u.username AS username, u.profile_image AS profile_image, " +
            "CASE " +
            "WHEN f2.from_user_id IS NULL THEN 0 " +
            "    ELSE 1 " +
            "END follow_flag " +
            "FROM follow f1 INNER JOIN user u ON f1.from_user_id = u.user_id " +
            "LEFT JOIN follow f2 " +
            "ON f2.from_user_id = :loginUserId AND f2.to_user_id = f1.from_user_id " +
            "WHERE f1.to_user_id = :toUserId", nativeQuery = true)
    List<Object[]> findFollowerUserList(@Param("loginUserId") Long loginUserId, @Param("toUserId") Long toUserId);

    @Query("SELECT f.toUser.id FROM Follow f WHERE f.fromUser = :fromUser")
    Slice<Long> findFollowingUserRecommend(@Param("fromUser") User fromUser, Pageable pageable);

    Long countByFromUserIs(User fromUser);

    Long countByToUserIs(User toUser);
}