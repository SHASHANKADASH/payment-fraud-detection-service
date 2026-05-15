package org.shashanka.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Builder
@Getter
public class PaymentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long accountId;
    private Double amount;
    private String merchant;
    private String status;
    private LocalDateTime createdAt;
}
