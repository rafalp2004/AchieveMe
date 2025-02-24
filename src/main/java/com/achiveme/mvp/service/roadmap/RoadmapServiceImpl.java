package com.achiveme.mvp.service.roadmap;

import com.achiveme.mvp.dto.roadmap.RoadmapInfoDTO;
import com.achiveme.mvp.dto.roadmap.RoadmapResponseDTO;
import com.achiveme.mvp.dto.roadmap.RoadmapUpdateDTO;
import com.achiveme.mvp.entity.Challenge;
import com.achiveme.mvp.entity.Roadmap;
import com.achiveme.mvp.entity.User;
import com.achiveme.mvp.exception.Challenge.ChallengeDoesNotExistException;
import com.achiveme.mvp.exception.User.UnauthorizedException;
import com.achiveme.mvp.exception.User.UserDoesNotExistException;
import com.achiveme.mvp.mapper.RoadmapMapper;
import com.achiveme.mvp.repository.ChallengeRepository;
import com.achiveme.mvp.repository.RoadmapRepository;
import com.achiveme.mvp.repository.UserRepository;
import com.achiveme.mvp.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class RoadmapServiceImpl implements RoadmapService {

    private final ChatClient chatClient;
    private final RoadmapRepository roadmapRepository;
    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;
    private final RoadmapMapper roadmapMapper;
    private final UserService userService;

    public RoadmapServiceImpl(ChatClient.Builder chatClient, RoadmapRepository roadmapRepository, ChallengeRepository challengeRepository, UserRepository userRepository, RoadmapMapper roadmapMapper, UserService userService) {
        this.chatClient = chatClient.build();
        this.roadmapRepository = roadmapRepository;
        this.challengeRepository = challengeRepository;
        this.userRepository = userRepository;
        this.roadmapMapper = roadmapMapper;
        this.userService = userService;
    }


    @Override
    public RoadmapResponseDTO createRoadmap(RoadmapInfoDTO roadmapDTO) {
        String message = """
                Edit
                             
                """;

        PromptTemplate promptTemplate = new PromptTemplate(message);
        Prompt prompt = promptTemplate.create(
                Map.of(
                        "purpose", roadmapDTO.purpose(),
                        "daysLeft", roadmapDTO.daysLeft(),
                        "currentSituation", roadmapDTO.currentSituation(),
                        "additionalInfo", roadmapDTO.additionalInfo()
                )
        );
        Roadmap roadmap = new Roadmap();

        String content = this.chatClient.prompt(prompt).call().content();

        User user = userRepository.findById(roadmapDTO.userId()).orElseThrow(() -> new UserDoesNotExistException("User not found"));
        Challenge challenge = challengeRepository.findById(roadmapDTO.challengeId()).orElseThrow(() -> new ChallengeDoesNotExistException("Challenge not found"));
        roadmap.setChallenge(challenge);
        roadmap.setUser(user);
        roadmap.setContent(content);

        roadmapRepository.save(roadmap);
        return roadmapMapper.roadmapToRoadmapDTO(roadmap);
    }

    @Override
    public RoadmapResponseDTO getRoadmap(int userId, int challengeId) {
        if (userService.getCurrentUser().getId() == userId) {
            Roadmap roadmap = roadmapRepository.findbyUserIdAndChallengeId(userId, challengeId);
            return roadmapMapper.roadmapToRoadmapDTO(roadmap);
        } else
            throw new UnauthorizedException("User with id " + userId + " is not a part of challenge with id " + challengeId);

    }

    @Override
    public RoadmapResponseDTO update(RoadmapUpdateDTO roadmapUpdateDTO) {
        if (userService.getCurrentUser().getId() == roadmapUpdateDTO.userId()) {
            Roadmap originalRoadmap = roadmapRepository.findbyUserIdAndChallengeId(roadmapUpdateDTO.userId(), roadmapUpdateDTO.challengeId());
            String message = "Here is the original roadmap:\n" + originalRoadmap.getContent() +
                    "\n\nUser instructions for update:\n" + roadmapUpdateDTO.infoForChange() +
                    "\n\nPlease update the roadmap accordingly.";
            String updatedContent = this.chatClient.prompt(message).call().content();
            originalRoadmap.setContent(updatedContent);
            roadmapRepository.save(originalRoadmap);
            return roadmapMapper.roadmapToRoadmapDTO(originalRoadmap);

        } else
            throw new UnauthorizedException("User with id " + roadmapUpdateDTO.userId() + " is not a part of challenge with id " + roadmapUpdateDTO.challengeId());



    }
}