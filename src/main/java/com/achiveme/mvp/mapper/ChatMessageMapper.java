package com.achiveme.mvp.mapper;

import com.achiveme.mvp.dto.challengeParticipant.ChallengeParticipantResponseDTO;
import com.achiveme.mvp.dto.websocket.ChatMessageDTO;
import com.achiveme.mvp.entity.ChallengeParticipant;
import com.achiveme.mvp.entity.ChatMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ChatMessageMapper {
    @Mapping(source = "challenge.id", target = "challengeId")
    @Mapping(source = "sender.id", target = "senderId")
    @Mapping(source="sender.firstName", target="senderName")
    ChatMessageDTO chatMessageTochatMessageDTO(ChatMessage chatMessage);
}
