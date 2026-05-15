package org.shashanka.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentRequest {
    private Long accountId;
    private Double amount;
    private String merchant;
}
