package com.achiveme.mvp.service.challengeParticipant;


import com.achiveme.mvp.dto.challengeParticipant.ChallengeParticipantResponseDTO;
import com.achiveme.mvp.dto.challengeParticipant.ChallengeParticipantUpdateDTO;

import java.util.List;

public interface ChallengeParticipantService {
    ChallengeParticipantResponseDTO createParticipant(int challengeId);

    List<ChallengeParticipantResponseDTO> getParticipantsByChallengeId(int id);

    void deleteParticipantById(int id, int participantId);

    ChallengeParticipantResponseDTO updateUserStatus(int challengeId, int userId, ChallengeParticipantUpdateDTO participantUpdateDTO);
}
