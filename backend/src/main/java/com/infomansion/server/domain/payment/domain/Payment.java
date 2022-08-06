package com.infomansion.server.domain.payment.domain;

import com.infomansion.server.domain.user.domain.UserCredit;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Payment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    private Long userId;

    private Long beforeCredit;
    private Long afterCredit;
    private Long totalPrice;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime paymentDate;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "payment")
    private List<PaymentLine> paymentLines = new ArrayList<>();

    @Builder
    public Payment(Long userId, Long beforeCredit, Long afterCredit, Long totalPrice) {
        this.userId = userId;
        this.beforeCredit = beforeCredit;
        this.afterCredit = afterCredit;
        this.totalPrice = totalPrice;
    }

    public static Payment record(UserCredit userCredit, long totalPrice) {
        return Payment.builder()
                .userId(userCredit.getUserId())
                .beforeCredit(userCredit.getCredit())
                .afterCredit(userCredit.getCredit() - totalPrice)
                .totalPrice(totalPrice)
                .build();
    }

    public void addPaymentLine(PaymentLine paymentLine) {
        this.paymentLines.add(paymentLine);
        paymentLine.oneOf(this);
    }
}
