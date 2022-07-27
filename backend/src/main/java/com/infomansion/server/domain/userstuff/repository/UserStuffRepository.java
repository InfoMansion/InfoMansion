package com.infomansion.server.domain.userstuff.repository;

import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserStuffRepository extends JpaRepository<UserStuff, Long> {

    @Query("select us from UserStuff us left join fetch us.stuff where us.deleteFlag = false")
    List<UserStuff> findByUser(User user);

    @Query("select us from UserStuff us left join fetch us.stuff where us.deleteFlag = false and us.id = :id")
    @Override
    Optional<UserStuff> findById(@Param("id") Long id);

    @Query("select distinct us.category from UserStuff us where us.user.id = :user_id and us.deleteFlag = false and us.selected = true")
    List<String> findAllCategoryByUserId(@Param("user_id") Long userId);
}
