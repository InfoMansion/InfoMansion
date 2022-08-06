package com.infomansion.server.domain.payment.domain;

import com.infomansion.server.domain.stuff.domain.Stuff;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PaymentLine {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stuff_id")
    private Stuff stuff;

    private Long price;

    @Builder
    protected PaymentLine(Payment payment, Stuff stuff, Long price) {
        this.payment = payment;
        this.stuff = stuff;
        this.price = price;
    }

    public static PaymentLine createPaymentLine(Payment payment, Stuff stuff, Long price) {
        return PaymentLine.builder()
                .payment(payment)
                .stuff(stuff)
                .price(price)
                .build();
    }

    public void oneOf(Payment payment) {
        this.payment = payment;
    }
}
