package com.achiveme.mvp.entity;

import com.achiveme.mvp.enums.ChallengePurpose;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Table(name="challenges")
@Entity
@Getter
@Setter
@ToString(exclude = "challengeParticipants")
@EqualsAndHashCode(exclude = "challengeParticipants")
public class Challenge {
    @Id
    @Column(name="challenge_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="creator_user_id", nullable = false)
    private User creatorUser;

    @Column(name="title")
    private String title;

    @Column(name="description")
    private String description;

    @Column(name="purpose")
    @Enumerated(EnumType.STRING)
    private ChallengePurpose purpose;

    @Column(name="start_at")
    private LocalDateTime startAt;

    @Column(name="deadline")
    private LocalDateTime deadline;

    @Column(name="entry_fee")
    private BigDecimal entryFee;

    @Column(name="is_public")
    private Boolean isPublic;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.REMOVE)
    Set<CheckPost> checkPosts;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.REMOVE)
    Set<ChallengeParticipant> challengeParticipants;

    public void addParticipant(ChallengeParticipant participant){
        if (challengeParticipants==null){
            challengeParticipants = new HashSet<>();
        }
        challengeParticipants.add(participant);
    }



}
