package com.achiveme.mvp.controller;

import com.achiveme.mvp.dto.websocket.ChatMessageDTO;
import com.achiveme.mvp.service.messages.ChatMessageService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    private ChatMessageService chatMessageService;

    public MessageController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/challenge/{challengeId}")
    @SendTo("/topic/challenge/{challengeId}")
    public ChatMessageDTO sendMessage(@DestinationVariable String challengeId, ChatMessageDTO chatMessage) {
        return chatMessageService.processMessage(chatMessage);
    }
}
