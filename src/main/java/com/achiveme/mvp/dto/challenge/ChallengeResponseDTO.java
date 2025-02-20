package com.achiveme.mvp.dto.challenge;

import com.achiveme.mvp.dto.challengeParticipant.ChallengeParticipantResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ChallengeResponseDTO(
        int id,
        int creatorUser,
        String title,
        // TODO Change that to existing ENUM
        String purpose,
        String description,
        LocalDateTime deadline,
        LocalDateTime startAt,
        BigDecimal entryFee,
        boolean isPublic,
        List<ChallengeParticipantResponseDTO> participants
) {
}
