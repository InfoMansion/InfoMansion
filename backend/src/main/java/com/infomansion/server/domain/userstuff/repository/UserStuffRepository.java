package com.infomansion.server.domain.userstuff.repository;

import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserStuffRepository extends JpaRepository<UserStuff, Long> {

    @Query("select us from UserStuff us join fetch us.stuff where us.deleteFlag = false and us.user = :user")
    List<UserStuff> findByUser(@Param("user") User user);

    @Query("select us from UserStuff us join fetch us.stuff where us.deleteFlag = false and us.id = :id")
    @Override
    Optional<UserStuff> findById(@Param("id") Long id);

    @Query("select distinct us.category from UserStuff us where us.user.id = :userId and us.deleteFlag = false and us.selected = true")
    List<String> findAllCategoryByUserId(@Param("userId") Long userId);

    @Query("select us.stuff.id from UserStuff us where us.user.id = :userId and us.deleteFlag = false")
    List<Long> findByUserId(@Param("userId") Long userId);

    @Query("select us from UserStuff us join fetch us.stuff where us.selected = true and us.deleteFlag = false and us.user = :user " +
            "order by (case when us.stuff.stuffType = 'WALL' then 1 " +
            "when us.stuff.stuffType = 'FLOOR' then 2 " +
            "else 3 end)")
    List<UserStuff> findArrangedByUser(@Param("user") User user);
}
