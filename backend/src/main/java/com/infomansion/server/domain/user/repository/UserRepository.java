package com.infomansion.server.domain.user.repository;

import com.infomansion.server.domain.post.domain.Post;
import com.infomansion.server.domain.user.domain.User;
import com.infomansion.server.domain.user.dto.UserSimpleProfileResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    @Query("select u from User u where u.username like %:searchWord% and not u.username = :loginUser")
    Slice<User> findUserByUserName(@Param("searchWord") String searchWord, @Param("loginUser") String loginUser, Pageable pageable);

    @Query("SELECT u FROM User u JOIN FETCH u.userCredit WHERE u.id = :userId")
    Optional<User> findUserWithCredit(@Param("userId") Long userId);

}
