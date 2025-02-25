package com.achiveme.mvp.dto.roadmap;

public record RoadmapResponseDTO(
        int userId,
        int challengeId,
        String content
) {
}
