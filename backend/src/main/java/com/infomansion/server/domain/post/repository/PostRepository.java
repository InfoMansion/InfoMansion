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
    @Query("select distinct p.user.id from Post p left join LikesPost l on p.id = l.postId where (p.category in :categories and p.modifiedDate between :start and :end and not p.user= :user) group by p.user.id order by l.likes desc")
    List<Long> findTop27ByCategoryInAndModifiedDateBetween(@Param("user") User user, @Param("categories") List<Category> categories, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("select p from Post p where p.userStuff = :userStuff order by p.modifiedDate")
    List<Post> findByUserStuffId(@Param("userStuff") UserStuff userStuff);

    @Query("select p from Post p where p.title like %:searchWord% and p.user.privateFlag=false")
    Slice<Post> findByTitle(@Param("searchWord") String searchWord, Pageable pageable);

    @Query("select p from Post p where p.content like %:searchWord% and p.user.privateFlag=false")
    Slice<Post> findByContent(@Param("searchWord") String searchWord, Pageable pageable);

    @Query("select p from Post p join fetch p.user where p.id=:id")
    Optional<Post> findPostWithUser(@Param("id") Long id);

    @Query("select p from Post p join fetch p.user where p.user.id = :userId")
    List<Post> findTop5Post(@Param("userId") Long userId, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update Post p set p.userStuff = :anotherUs where p.userStuff = :us")
    int movePostToAnotherStuff(@Param("us") UserStuff us, @Param("anotherUs") UserStuff anotherUs);
}