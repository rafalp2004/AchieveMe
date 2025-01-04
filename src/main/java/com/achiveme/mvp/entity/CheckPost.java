package com.achiveme.mvp.entity;

import com.achiveme.mvp.enums.CheckPostStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "check_posts")
@Getter
@Setter
public class CheckPost {
    @Id
    @Column(name = "check_post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CheckPostStatus checkPostStatus;

    @Column(name = "published_date")
    private LocalDateTime publishedDate;

}
