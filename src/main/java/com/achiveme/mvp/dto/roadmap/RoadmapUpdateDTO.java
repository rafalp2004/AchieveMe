package com.achiveme.mvp.dto.roadmap;

public record RoadmapUpdateDTO(
        int userId,
        int challengeId,
        String infoForChange
) {
}
