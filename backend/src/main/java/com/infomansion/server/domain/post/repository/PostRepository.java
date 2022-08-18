package com.infomansion.server.domain.post.repository;

import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.domain.post.domain.Post;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>{

    // 후에 UserLikePost 데이터가 채워지면 inner join으로 변경 예정
    @Query(value = "select distinct p.user.id from Post p left join UserLikePost ulp on p.id = ulp.post.id " +
            "where p.modifiedDate BETWEEN :start and :end " +
            "and p.category in :categories and not p.user = :user " +
            "and not p.category = 'GUESTBOOK'" +
            "group by p.id " +
            "order by count (ulp.post) desc")
    Slice<Long> findTop27PostByUserLikePost(@Param("user") User user, @Param("categories") List<Category> categories, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end, Pageable pageable);

    @Query("select p from Post p where p.userStuff = :userStuff order by p.modifiedDate")
    Slice<Post> findByUserStuffId(@Param("userStuff") UserStuff userStuff, Pageable pageable);

    @Query("select p from Post p where p.title like %:searchWord% and not p.category = 'GUESTBOOK' and p.user.privateFlag=false")
    Slice<Post> findByTitle(@Param("searchWord") String searchWord, Pageable pageable);

    @Query("select p from Post p where p.content like %:searchWord% and not p.category = 'GUESTBOOK' and p.user.privateFlag=false")
    Slice<Post> findByContent(@Param("searchWord") String searchWord, Pageable pageable);

    @Query("select p from Post p join fetch p.user where p.id=:id")
    Optional<Post> findPostWithUser(@Param("id") Long id);

    @Query("select p from Post p join fetch p.user where p.user.id = :userId and not p.category = 'GUESTBOOK'")
    List<Post> findTop5Post(@Param("userId") Long userId, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update Post p set p.userStuff = :anotherUs, p.category = null where p.userStuff = :us")
    int movePostToAnotherStuff(@Param("us") UserStuff us, @Param("anotherUs") UserStuff anotherUs);

    @Modifying(clearAutomatically = true)
    @Query("update Post p set p.userStuff = :anotherUs, p.category = null where p.userStuff in :us")
    int movePostToAnotherStuffs(@Param("us") List<UserStuff> us, @Param("anotherUs") UserStuff anotherUs);

    @Query("SELECT p FROM Post p WHERE p.user.id = :userId AND p.isPublished = false")
    List<Post> findTempPostsByUserId(@Param("userId") Long userId);

    @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.userStuff = :guestbook")
    Slice<Post> findPostInTheGuestBook(@Param("guestbook") UserStuff guestbook, Pageable pageable);

    int countByUserAndModifiedDateBetween(User user, LocalDateTime startDate, LocalDateTime endDate);

}