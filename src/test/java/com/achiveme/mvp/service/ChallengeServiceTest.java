package com.achiveme.mvp.service;

import com.achiveme.mvp.dto.challenge.ChallengeCreateRequestDTO;
import com.achiveme.mvp.dto.challenge.ChallengeResponseDTO;
import com.achiveme.mvp.entity.Challenge;
import com.achiveme.mvp.entity.User;
import com.achiveme.mvp.enums.ChallengePurpose;
import com.achiveme.mvp.mapper.ChallengeMapper;
import com.achiveme.mvp.repository.ChallengeRepository;
import com.achiveme.mvp.service.challenge.ChallengeServiceImpl;
import com.achiveme.mvp.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ChallengeServiceTest {
    @Mock
    private ChallengeRepository challengeRepository;

    @Mock
    private UserService userService;

    @Mock
    private ChallengeMapper challengeMapper;

    @InjectMocks
    private ChallengeServiceImpl challengeService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateChallenge(){
        // Arrange
        ChallengeCreateRequestDTO challengeDTO = new ChallengeCreateRequestDTO(// id
                "title", // title
                ChallengePurpose.LOSE_WEIGHT, // purpose
                "description", // description
                LocalDateTime.now().plusDays(10), // deadline
                LocalDateTime.now().plusDays(1), // startAt
                BigDecimal.valueOf(100.0), // entryFee
                true // isPublic

        );
        User mockUser = new User();
        mockUser.setId(1);


        Challenge mockChallenge = new Challenge();
        ChallengeResponseDTO expectedResponse = new ChallengeResponseDTO(
                1, // id
                1, // creatorUser
                "title", // title
                "purpose", // purpose
                "description", // description
                LocalDateTime.now().plusDays(10), // deadline
                LocalDateTime.now().plusDays(1), // startAt
                BigDecimal.valueOf(100.0), // entryFee
                true, // isPublic
                List.of() // participants
        );
        when(userService.getCurrentUser()).thenReturn(mockUser);
        when(challengeRepository.save(any(Challenge.class))).thenReturn(mockChallenge);
        when(challengeMapper.challengeToChallengeDTO(any(Challenge.class))).thenReturn(expectedResponse);

        // Debugowanie
        System.out.println("Mock user: " + userService.getCurrentUser());
        System.out.println("Mock challenge after save: " + challengeRepository.save(new Challenge()));
        System.out.println("Mock mapper response: " + challengeMapper.challengeToChallengeDTO(mockChallenge));




        // ACT
        ChallengeResponseDTO result = challengeService.createChallenge(challengeDTO);

        // ASSERT
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).isEqualTo(expectedResponse);

    }

}
