package org.shashanka.service;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.shashanka.exception.InsufficientBalanceException;
import org.shashanka.exception.ResourceNotFoundException;
import org.shashanka.domain.PaymentRequest;
import org.shashanka.domain.PaymentResponse;
import org.shashanka.entity.AccountModel;
import org.shashanka.entity.PaymentModel;
import org.shashanka.repository.AccountRepository;
import org.shashanka.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Log4j2
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final AccountRepository accountRepository;

    public PaymentService(PaymentRepository paymentRepository, AccountRepository accountRepository) {
        this.paymentRepository = paymentRepository;
        this.accountRepository = accountRepository;
    }

    // follows proxy pattern in spring. Enables atomicity
    @Transactional
    public PaymentResponse processPayment(final PaymentRequest payment) {
        log.info("Thread: {}", Thread.currentThread().getName());
        final AccountModel account = accountRepository.findById(payment.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        final double remainingBalance = account.getBalance() - payment.getAmount();
        if(remainingBalance < 0) {
            throw new InsufficientBalanceException("Insufficient Balance");
        }
        log.info("Balance before update {}", account.getBalance());
        account.setBalance(remainingBalance);
        accountRepository.save(account);
        log.info("Balance after update {}", account.getBalance());
        final PaymentModel paymentModel = PaymentModel.builder()
                .amount(payment.getAmount())
                .accountId(account.getId())
                .merchant(payment.getMerchant())
                .status("SUCCESS")
                .createdAt(LocalDateTime.now())
                .build();
        paymentRepository.save(paymentModel);
        return PaymentResponse.builder().paymentId(paymentModel.getId())
                .status(paymentModel.getStatus())
                .remainingBalance(account.getBalance()).build();
    }
}
