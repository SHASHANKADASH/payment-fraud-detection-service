package org.shashanka.controller;

import org.shashanka.domain.PaymentRequest;
import org.shashanka.domain.PaymentResponse;
import org.shashanka.domain.SimulationRequest;
import org.shashanka.service.PaymentService;
import org.shashanka.service.PaymentSimulationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentSimulationService paymentSimulationService;

    public PaymentController(PaymentService paymentService, PaymentSimulationService paymentSimulationService) {
        this.paymentService = paymentService;
        this.paymentSimulationService = paymentSimulationService;
    }

    @PostMapping
    public PaymentResponse processPayment(final @RequestBody PaymentRequest payment) {
        PaymentResponse paymentResponse = paymentService.processPayment(payment);
        System.out.println(paymentResponse);
        return paymentResponse;
    }

    @PostMapping("/simulate")
    public String simulate(final @RequestBody SimulationRequest simulationRequest) {
        paymentSimulationService.simulate(simulationRequest);
        return "Success";
    }
}
