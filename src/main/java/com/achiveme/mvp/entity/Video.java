package com.achiveme.mvp.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Table(name = "videos")
@Entity
@Data
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "is_verified")
    private Boolean isVerified;

    @OneToOne
    @JoinColumn(name = "verified_by")
    private User verifiedBy;

    @Column(name = "verification_date")
    private LocalDateTime verificationDate;

}
