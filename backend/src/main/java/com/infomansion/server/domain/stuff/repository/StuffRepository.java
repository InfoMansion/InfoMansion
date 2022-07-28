package com.infomansion.server.domain.stuff.repository;

import com.infomansion.server.domain.stuff.domain.Stuff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StuffRepository extends JpaRepository<Stuff, Long> {

    @Override
    @Query("select s from Stuff s where s.deleteFlag = false and s.id = :id")
    Optional<Stuff> findById(@Param("id") Long id);

    @Override
    @Query("select s from Stuff s where s.deleteFlag = false")
    List<Stuff> findAll();
}
