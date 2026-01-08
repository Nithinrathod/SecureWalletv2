package org.webapp.walletservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walletId;
    @Column(unique = true, nullable = false)
    private String userId;
    private double balance;
    private String status; //ACTIVE OR BLOCKED
}