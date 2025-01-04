package com.achiveme.mvp.repository;

import com.achiveme.mvp.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Integer> {
}
