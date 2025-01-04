package com.achiveme.mvp.entity;

import com.achiveme.mvp.enums.ParticipantStatus;
import com.achiveme.mvp.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Table(name="challenge_participants")
@Data
@ToString(exclude = "challenge")
@Entity
public class ChallengeParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="participant_id")
    private int id;

    @ManyToOne
    @JoinColumn(name="challenge_id")
    private Challenge challenge;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(name="join_date")
    private LocalDateTime joinDate;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private ParticipantStatus participantStatus;

    @Column(name="payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
}
