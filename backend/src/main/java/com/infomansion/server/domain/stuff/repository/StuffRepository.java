package com.infomansion.server.domain.stuff.repository;

import com.infomansion.server.domain.stuff.domain.Stuff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface StuffRepository extends JpaRepository<Stuff, Long> {

    @Override
    @Query("select s from Stuff s where s.deleteFlag = false and s.id = :id")
    Optional<Stuff> findById(@Param("id") Long id);

    @Override
    @Query("select s from Stuff s where s.deleteFlag = false")
    List<Stuff> findAll();

    @Query(value =
            "SELECT *" +
            "FROM " +
            "(SELECT *, RANK() OVER(PARTITION BY s.stuff_type ORDER BY s.stuff_id) AS rk " +
            "FROM stuff AS s " +
            "WHERE s.stuff_id NOT IN (" +
            "SELECT DISTINCT us.stuff_id " +
            "FROM user_stuff AS us " +
            "WHERE us.user_id = :loginUserId)" +
            ") AS sr " +
            "WHERE sr.rk <= :pageSize", nativeQuery = true)
    List<Stuff> findAllStuffInStore(@Param("loginUserId") Long userId, @Param("pageSize") Integer pageSize);

    @Query(value =
            "SELECT *" +
            "FROM " +
            "(SELECT *, RANK() OVER(PARTITION BY s.stuff_type ORDER BY s.stuff_id) AS rk " +
            "FROM stuff AS s " +
            "WHERE s.stuff_type = :stuffType AND s.stuff_id NOT IN (" +
            "SELECT DISTINCT us.stuff_id " +
            "FROM user_stuff AS us " +
            "WHERE us.user_id = :loginUserId) " +
            ") AS sr " +
            "ORDER BY sr.rk " +
            "LIMIT :offset, :pageSize", nativeQuery = true)
    List<Stuff> findStuffWithStuffTypeInStore(@Param("loginUserId") Long userId, @Param("stuffType") String stuffType, @Param("offset") Long offset, @Param("pageSize") Integer pageSize);
}
