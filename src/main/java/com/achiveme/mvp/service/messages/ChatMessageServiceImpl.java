package com.achiveme.mvp.service.messages;

import com.achiveme.mvp.dto.websocket.ChatMessageDTO;
import com.achiveme.mvp.entity.Challenge;
import com.achiveme.mvp.entity.ChatMessage;
import com.achiveme.mvp.entity.User;
import com.achiveme.mvp.exception.Challenge.ChallengeDoesNotExistException;
import com.achiveme.mvp.exception.User.UserDoesNotExistException;
import com.achiveme.mvp.mapper.ChatMessageMapper;
import com.achiveme.mvp.repository.ChallengeRepository;
import com.achiveme.mvp.repository.ChatMessageRepository;
import com.achiveme.mvp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final ChatMessageMapper chatMessageMapper;

    public ChatMessageServiceImpl(ChatMessageRepository chatMessageRepository, UserRepository userRepository, ChallengeRepository challengeRepository, ChatMessageMapper chatMessageMapper) {
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
        this.challengeRepository = challengeRepository;
        this.chatMessageMapper = chatMessageMapper;
    }

    @Override
    public ChatMessageDTO processMessage(ChatMessageDTO messageDTO) {
        ChatMessage chatMessage = new ChatMessage();

        User user = userRepository.findById(messageDTO.senderId()).orElseThrow(() -> new UserDoesNotExistException("User not found"));
        Challenge challenge = challengeRepository.findById(messageDTO.challengeId()).orElseThrow(() -> new ChallengeDoesNotExistException("Challenge not found"));

        chatMessage.setSender(user);
        chatMessage.setChallenge(challenge);
        chatMessage.setContent(messageDTO.content());
        chatMessage.setTimestamp(LocalDateTime.now());

        return chatMessageMapper.chatMessageTochatMessageDTO(chatMessage);
    }
}
