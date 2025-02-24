package com.achiveme.mvp.repository;

import com.achiveme.mvp.entity.Roadmap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoadmapRepository extends JpaRepository<Roadmap, Integer> {

    @Query(value="SELECT r FROM Roadmap r WHERE r.user.id =:userId AND r.challenge.id =:challengeId")
    Roadmap findbyUserIdAndChallengeId(@Param("userId") int userId,@Param("challengeId") int challengeId);
}
