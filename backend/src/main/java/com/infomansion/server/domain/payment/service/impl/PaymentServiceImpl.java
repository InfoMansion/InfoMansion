package com.infomansion.server.domain.payment.service.impl;

import com.infomansion.server.domain.payment.domain.PaymentLine;
import com.infomansion.server.domain.payment.dto.PaymentResponseDto;
import com.infomansion.server.domain.payment.repository.PaymentRepository;
import com.infomansion.server.domain.payment.service.PaymentService;
import com.infomansion.server.global.util.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public List<PaymentResponseDto> findPaymentLinesByLoginUser(int page) {
        return paymentRepository.findAllPaymentByUserIdDesc(SecurityUtil.getCurrentUserId(), PageRequest.of(page-1, 10, Sort.by("paymentDate").descending()))
                .stream().map(PaymentResponseDto::new).collect(Collectors.toList());
    }
}
