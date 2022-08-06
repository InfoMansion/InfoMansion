package com.infomansion.server.domain.user.repository;

import com.infomansion.server.domain.user.domain.UserCredit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCreditRepository extends JpaRepository<UserCredit, Long> {
}
