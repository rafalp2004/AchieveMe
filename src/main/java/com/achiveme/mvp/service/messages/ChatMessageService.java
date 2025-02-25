package com.achiveme.mvp.service.messages;

import com.achiveme.mvp.dto.websocket.ChatMessageDTO;

public interface ChatMessageService {
    ChatMessageDTO processMessage(ChatMessageDTO messageDTO);
}
