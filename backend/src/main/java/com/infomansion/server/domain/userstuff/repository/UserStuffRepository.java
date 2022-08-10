package com.infomansion.server.domain.userstuff.repository;

import com.infomansion.server.domain.category.domain.Category;
import com.infomansion.server.domain.stuff.domain.StuffType;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserStuffRepository extends JpaRepository<UserStuff, Long> {

    @Query("select us from UserStuff us join fetch us.stuff where us.user = :user")
    List<UserStuff> findByUser(@Param("user") User user);

    @Query("select us from UserStuff us join fetch us.stuff where us.id = :id")
    @Override
    Optional<UserStuff> findById(@Param("id") Long id);

    @Query("select distinct us.category from UserStuff us where us.user.id = :userId and us.selected = true")
    List<String> findAllCategoryByUserId(@Param("userId") Long userId);

    @Query("select us.stuff.id from UserStuff us where us.user.id = :userId")
    List<Long> findByUserId(@Param("userId") Long userId);

    @Query("select us from UserStuff us join fetch us.stuff where us.selected = true and us.user = :user " +
            "order by (case when us.stuff.stuffType = 'WALL' then 1 " +
            "when us.stuff.stuffType = 'FLOOR' then 2 " +
            "else 3 end)")
    List<UserStuff> findArrangedByUser(@Param("user") User user);

    @Query("select us from UserStuff us where us.user = :user and us.selected = true and us.category <> :category ")
    List<UserStuff> findCategoryPlacedInRoom(@Param("user") User user, @Param("category") Category category);

    List<UserStuff> findByUserIsAndIdNotInAndSelectedIsTrue(User user, List<Long> userStuffIds);

    @Query("select us from UserStuff us join fetch us.stuff where us.user.id = :userId and us.stuff.stuffType = :stuffType")
    Optional<UserStuff> findUserStuffByStuffType(@Param("userId")Long userId, @Param("stuffType") StuffType stuffType);
}
