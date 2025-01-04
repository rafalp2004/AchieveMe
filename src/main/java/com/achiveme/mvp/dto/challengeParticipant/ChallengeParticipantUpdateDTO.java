package com.achiveme.mvp.dto.challengeParticipant;

import com.achiveme.mvp.enums.ParticipantStatus;
import com.achiveme.mvp.enums.PaymentStatus;

public record ChallengeParticipantUpdateDTO(
       //TODO sprawdzic czy tak moze byc
        ParticipantStatus participantStatus,
        PaymentStatus paymentStatus
) {
}
