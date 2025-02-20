package com.achiveme.mvp.dto.checkPost;

import com.achiveme.mvp.enums.CheckPostStatus;

import java.time.LocalDateTime;

public record CheckPostResponseDTO(
        int id,
        int challengeId,
        String title,
        String description,
        CheckPostStatus checkPostStatus,
        LocalDateTime publishedDate

) {
}
