package com.infomansion.server.domain.post.repository;

import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>{
    @Query("select distinct p.user.id from Post p left join LikesPost l on p.id = l.id where (p.category in :categories and p.modifiedDate between :start and :end and not p.user.id = :userId) group by p.user.id order by l.likes desc")
    List<Long> findTop13ByCategoryInAndModifiedDateBetween(@Param("userId") Long userId, @Param("categories") List<Category> categories, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);


}