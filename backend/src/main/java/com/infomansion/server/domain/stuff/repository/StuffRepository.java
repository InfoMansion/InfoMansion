package com.infomansion.server.domain.stuff.repository;

import com.infomansion.server.domain.stuff.domain.Stuff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StuffRepository extends JpaRepository<Stuff, Long> {

}
