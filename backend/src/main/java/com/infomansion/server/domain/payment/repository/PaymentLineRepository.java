package com.infomansion.server.domain.payment.repository;

import com.infomansion.server.domain.payment.domain.Payment;
import com.infomansion.server.domain.payment.domain.PaymentLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentLineRepository extends JpaRepository<PaymentLine, Long> {

    @Query("SELECT pl FROM PaymentLine pl JOIN FETCH pl.stuff WHERE pl.payment = :payment")
    List<PaymentLine> findPaymentLinesByPaymentAndUser(@Param("payment")Payment payment);
}
