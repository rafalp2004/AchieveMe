package com.achiveme.mvp.repository;

import com.achiveme.mvp.entity.ChallengeParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeParticipantRepository extends JpaRepository<ChallengeParticipant, Integer> {
}
