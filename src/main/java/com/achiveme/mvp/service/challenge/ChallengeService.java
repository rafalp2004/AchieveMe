package com.achiveme.mvp.service.challenge;

import com.achiveme.mvp.dto.challenge.ChallengeCreateRequestDTO;
import com.achiveme.mvp.dto.challenge.ChallengeResponseDTO;
import com.achiveme.mvp.dto.challenge.ChallengeUpdateRequestDTO;

import java.util.List;

public interface ChallengeService {
    ChallengeResponseDTO createChallenge(ChallengeCreateRequestDTO challengeDTO);
    ChallengeResponseDTO getChallengeById(int id);

    List<ChallengeResponseDTO> getAllChallenges();

    void deleteChallengeById(int id);

    ChallengeResponseDTO updateChallenge(int id, ChallengeUpdateRequestDTO challengeDTO);

    boolean userIsParticipant(int challengeId, int userId);
}
