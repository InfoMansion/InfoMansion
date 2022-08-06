package com.infomansion.server.domain.user.domain;

import com.infomansion.server.global.util.exception.CustomException;
import com.infomansion.server.global.util.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserCredit {

    @Id
    private Long userId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Long credit;

    public UserCredit(User user) {
        this.user = user;
        this.credit = 0L;
    }

    public void earnCredit(Long amount) {
        if(amount <= 0) throw new CustomException(ErrorCode.NEGATIVE_AMOUNT_OF_CREDIT);
        this.credit += amount;
    }

    public void spend(Long stuffPrice) {
        if(this.credit < stuffPrice) throw new CustomException(ErrorCode.NOT_ENOUGH_CREDIT);
        this.credit -= stuffPrice;
    }

    @Override
    public String toString() {
        return "UserCredit{" +
                "userId=" + userId +
                ", credit=" + credit +
                '}';
    }
}
