package org.shashanka.controller;

import org.shashanka.domain.PaymentRequest;
import org.shashanka.domain.PaymentResponse;
import org.shashanka.service.PaymentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public PaymentResponse processPayment(final @RequestBody PaymentRequest payment) {
        PaymentResponse paymentResponse = paymentService.processPayment(payment);
        System.out.println(paymentResponse);
        return paymentResponse;
    }
}
