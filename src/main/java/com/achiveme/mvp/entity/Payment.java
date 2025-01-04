package com.achiveme.mvp.entity;

import com.achiveme.mvp.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name="payments")
@Entity
@Data
public class Payment{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="payment_id")
    private int id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="challange_id")
    private Challenge challenge;

    @Column(name="amount")
    private BigDecimal amount;

    @Column(name="payment_date")
    private LocalDateTime paymentDate;

    @Column(name="payment_status")
    private PaymentStatus paymentStatus;



}
