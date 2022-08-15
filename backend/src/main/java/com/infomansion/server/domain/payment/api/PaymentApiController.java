package com.infomansion.server.domain.payment.api;

import com.infomansion.server.domain.payment.service.PaymentService;
import com.infomansion.server.global.apispec.BasicResponse;
import com.infomansion.server.global.apispec.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class PaymentApiController {

    private final PaymentService paymentService;

    @GetMapping("/api/v1/payment")
    public ResponseEntity<? extends BasicResponse> userPayments(@Valid @RequestParam int page) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(paymentService.findPaymentLinesByLoginUser(page)));
    }
}
