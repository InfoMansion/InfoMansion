package com.infomansion.server.domain.payment.repository;

import com.infomansion.server.domain.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT p FROM Payment p JOIN FETCH p.paymentLines WHERE p.id = :id")
    Optional<Payment> findAllPaymentLineById(@Param("id") Long id);

    @Query("SELECT p FROM Payment p JOIN FETCH p.paymentLines WHERE p.userId = :userId")
    List<Payment> findAllPaymentByUser(@Param("userId") Long userId);
}
