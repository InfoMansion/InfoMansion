package com.infomansion.server.domain.stuff.repository;

import com.infomansion.server.domain.stuff.domain.Stuff;
import com.infomansion.server.domain.stuff.domain.StuffType;
import com.infomansion.server.domain.stuff.dto.StoreResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StuffRepository extends JpaRepository<Stuff, Long> {

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

    Slice<StoreResponseDto> findByStuffTypeAndIdNotIn(StuffType stuffType, List<Long> stuffIds, Pageable pageable);

    List<Stuff> findStuffByIdIn(List<Long> defaultStuffIds);

    List<Stuff> findTop10ByOrderByCreatedDateDesc();
}
