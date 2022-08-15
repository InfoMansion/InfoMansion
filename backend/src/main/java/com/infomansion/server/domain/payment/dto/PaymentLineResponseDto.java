package com.infomansion.server.domain.payment.dto;

import com.infomansion.server.domain.payment.domain.PaymentLine;
import com.infomansion.server.domain.stuff.dto.StuffResponseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PaymentLineResponseDto {
    private Long id;
    private StuffResponseDto stuff;
    private Long price;

    @Builder
    public PaymentLineResponseDto(Long id, StuffResponseDto stuff, Long price) {
        this.id = id;
        this.stuff = stuff;
        this.price = price;
    }

    public PaymentLineResponseDto(PaymentLine paymentLine) {
        this.id = paymentLine.getId();
        this.stuff = new StuffResponseDto(paymentLine.getStuff());
        this.price = paymentLine.getPrice();
    }
}
