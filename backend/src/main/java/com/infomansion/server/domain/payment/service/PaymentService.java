package com.infomansion.server.domain.payment.service;

import com.infomansion.server.domain.payment.dto.PaymentResponseDto;

import java.util.List;

public interface PaymentService {
    List<PaymentResponseDto> findPaymentLinesByLoginUser(int page);
}
