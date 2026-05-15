package org.shashanka.fraud.service;

import lombok.extern.log4j.Log4j2;
import org.shashanka.fraud.domain.UserRiskProfile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@Service
@Log4j2
public class FraudService {
    private final Map<Long, UserRiskProfile> riskCache = new ConcurrentHashMap<>();

    public boolean runFraudChecks(final Long accountId, Double amount, final String merchant) {
        updateRiskCache(accountId, amount);
        final CompletableFuture<Boolean> velocityCompletableFuture = CompletableFuture.supplyAsync(
                () -> velocity(accountId)
        );
        final CompletableFuture<Boolean> amountCompletableFuture = CompletableFuture.supplyAsync(
                () -> amountCheck(amount)
        );
        final CompletableFuture<Boolean> merchantCompletableFuture = CompletableFuture.supplyAsync(
                () -> merchantCheck(merchant)
        );
        try {
            return velocityCompletableFuture.get() && merchantCompletableFuture.get() && amountCompletableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e);
            return false;
        }
    }

    private boolean velocity(final Long accountId) {
        log.info("Check executed by {}", Thread.currentThread().getName());
        return riskCache.get(accountId).getTransactionCount() <= 5;
    }

    private void updateRiskCache(Long accountId, Double amount) {
        riskCache.compute(
                accountId, (id, profile) -> {
                    if (Objects.isNull(profile)) {
                        profile = UserRiskProfile.builder().transactionCount(0).totalAmount(0D)
                                .lastTransactionTime(LocalDateTime.now()).build();
                    }
                    profile.setTransactionCount(profile.getTransactionCount() + 1);
                    profile.setTotalAmount(profile.getTotalAmount() + amount);
                    profile.setLastTransactionTime(LocalDateTime.now());
                    return profile;
                }
        );
        log.info(riskCache);
    }

    private boolean amountCheck(final Double amount) {
        log.info("Check executed by {}", Thread.currentThread().getName());
        return amount < 5000;
    }

    private boolean merchantCheck(String merchant) {
        log.info("Check executed by {}", Thread.currentThread().getName());
        return !merchant.equalsIgnoreCase("SCAM");
    }
}
