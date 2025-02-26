package com.achiveme.mvp.dto.websocket;

import java.time.LocalDateTime;

public record ChatMessageDTO(
        Long id,
        String content,
        LocalDateTime timestamp,
        int senderId,
        String senderName,
        int challengeId
) {
}
