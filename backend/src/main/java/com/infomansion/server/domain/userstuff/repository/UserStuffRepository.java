package com.infomansion.server.domain.userstuff.repository;

import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.userstuff.domain.UserStuff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserStuffRepository extends JpaRepository<UserStuff, Long> {

    @Query("select us from UserStuff us join fetch us.stuff")
    public UserStuff findByUserAndStuff(User user, Stuff stuff);

    @Query("select us from UserStuff us join fetch us.stuff")
    public List<UserStuff> findByUser(User user);
}
