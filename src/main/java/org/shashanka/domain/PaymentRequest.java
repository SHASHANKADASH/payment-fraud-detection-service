package org.shashanka.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
    private Long accountId;
    private Double amount;
    private String merchant;
}
