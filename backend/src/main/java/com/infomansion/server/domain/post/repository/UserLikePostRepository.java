package com.infomansion.server.domain.post.repository;

import com.infomansion.server.domain.post.domain.Post;
import com.infomansion.server.domain.post.domain.UserLikePost;
import com.infomansion.server.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserLikePostRepository extends JpaRepository<UserLikePost, Long> {

    // 사용자가 좋아요를 누른 포스트들
    @Query(value = "SELECT ulp FROM UserLikePost ulp JOIN FETCH ulp.post WHERE ulp.user = :user")
    List<UserLikePost> findPostsByUserLike(@Param("user") User user);

    // 포스트에 좋아요를 누른 사용자들
    @Query(value = "SELECT ulp FROM UserLikePost ulp JOIN FETCH ulp.user WHERE ulp.post = :post")
    List<UserLikePost> findUsersLikeThisPost(@Param("post") Post post);

    Optional<UserLikePost> findByPostAndUser(Post post, User user);
}
