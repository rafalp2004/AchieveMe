package com.achiveme.mvp.dto.challengeParticipant;

import com.achiveme.mvp.enums.ParticipantStatus;
import com.achiveme.mvp.enums.PaymentStatus;

import java.time.LocalDateTime;

public record ChallengeParticipantResponseDTO(
        int id,
        int challengeId,
        int userId,
        LocalDateTime joinDate,
        ParticipantStatus participantStatus,
        PaymentStatus paymentStatus
) {
}
