package com.infomansion.server.domain.payment.dto;

import com.infomansion.server.domain.payment.domain.Payment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PaymentResponseDto {
    private Long id;
    private Long beforeCredit;
    private Long afterCredit;
    private Long totalPrice;
    private LocalDateTime paymentDate;
    private List<PaymentLineResponseDto> paymentLines;

    @Builder
    public PaymentResponseDto(Long id, Long beforeCredit, Long afterCredit, Long totalPrice, LocalDateTime paymentDate, List<PaymentLineResponseDto> paymentLines) {
        this.id = id;
        this.beforeCredit = beforeCredit;
        this.afterCredit = afterCredit;
        this.totalPrice = totalPrice;
        this.paymentDate = paymentDate;
        this.paymentLines = paymentLines;
    }

    public PaymentResponseDto(Payment payment) {
        this.id = payment.getId();
        this.beforeCredit = payment.getBeforeCredit();
        this.afterCredit = payment.getAfterCredit();
        this.totalPrice = payment.getTotalPrice();
        this.paymentDate = payment.getPaymentDate();
        this.paymentLines = payment.getPaymentLines().stream().map(PaymentLineResponseDto::new).collect(Collectors.toList());
    }
}
