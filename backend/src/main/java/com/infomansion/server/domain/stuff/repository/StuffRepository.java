package com.infomansion.server.domain.stuff.repository;

import com.infomansion.server.domain.stuff.domain.Stuff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StuffRepository extends JpaRepository<Stuff, Long> {

    @Override
    @Query("select s from Stuff s left join fetch s.stuffFile where s.id = :id")
    Optional<Stuff> findById(@Param("id") Long id);

    @Override
    @Query("select s from Stuff s left join fetch s.stuffFile")
    List<Stuff> findAll();
}
