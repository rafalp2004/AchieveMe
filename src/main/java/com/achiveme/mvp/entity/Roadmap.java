package com.achiveme.mvp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Table(name="roadmaps")
@Entity
@Data
public class Roadmap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="roadmap_id")
    private int id;

    @Column(name="content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
