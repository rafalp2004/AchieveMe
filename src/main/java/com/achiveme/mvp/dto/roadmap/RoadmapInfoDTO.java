package com.achiveme.mvp.dto.roadmap;

import java.time.LocalDate;

public record RoadmapInfoDTO(
        int userId,
        int challengeId,
        String purpose,
        int daysLeft,
        String currentSituation,
        String additionalInfo
) {
}
