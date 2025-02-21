package com.achiveme.mvp.service;

import com.achiveme.mvp.dto.challenge.ChallengeCreateRequestDTO;
import com.achiveme.mvp.dto.challenge.ChallengeResponseDTO;
import com.achiveme.mvp.dto.challenge.ChallengeUpdateRequestDTO;
import com.achiveme.mvp.entity.Challenge;
import com.achiveme.mvp.entity.ChallengeParticipant;
import com.achiveme.mvp.entity.User;
import com.achiveme.mvp.enums.ChallengePurpose;
import com.achiveme.mvp.mapper.ChallengeMapper;
import com.achiveme.mvp.repository.ChallengeRepository;
import com.achiveme.mvp.service.challenge.ChallengeServiceImpl;
import com.achiveme.mvp.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
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


    @Test
    public void testCreateChallenge() {
        // Arrange
        ChallengeCreateRequestDTO challengeDTO = new ChallengeCreateRequestDTO(
                "title",
                ChallengePurpose.LOSE_WEIGHT,
                "description",
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(1),
                BigDecimal.valueOf(100.0),
                true
        );
        User mockUser = new User();
        mockUser.setId(1);

        Challenge mockChallenge = new Challenge();
        mockChallenge.setId(1); // Simulate DB-assigned ID
        mockChallenge.setCreatorUser(mockUser);
        mockChallenge.setTitle(challengeDTO.title());
        mockChallenge.setPurpose(challengeDTO.purpose());
        mockChallenge.setDescription(challengeDTO.description());
        mockChallenge.setDeadline(challengeDTO.deadline());
        mockChallenge.setStartAt(challengeDTO.startAt());
        mockChallenge.setEntryFee(challengeDTO.entryFee());
        mockChallenge.setIsPublic(challengeDTO.isPublic());
        mockChallenge.setCreatedAt(LocalDateTime.now());

        ChallengeResponseDTO expectedResponse = new ChallengeResponseDTO(
                1, // id
                1, // creatorUser
                "title",
                "LOSE_WEIGHT", // Use the actual string value of the enum
                "description",
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(1),
                BigDecimal.valueOf(100.0),
                true,
                List.of()
        );

        when(userService.getCurrentUser()).thenReturn(mockUser);
        when(challengeRepository.save(any(Challenge.class))).thenReturn(mockChallenge);
        when(challengeMapper.challengeToChallengeDTO(any(Challenge.class))).thenReturn(expectedResponse);

        // Act
        ChallengeResponseDTO response = challengeService.createChallenge(challengeDTO);

        // Assert
        verify(userService).getCurrentUser();
        verify(challengeRepository).save(any(Challenge.class));
        verify(challengeMapper).challengeToChallengeDTO(any(Challenge.class));

        assertNotNull(response);
        assertEquals(expectedResponse.id(), response.id());
        assertEquals(expectedResponse.creatorUser(), response.creatorUser());
        assertEquals(expectedResponse.title(), response.title());
        assertEquals(expectedResponse.purpose(), response.purpose());
        assertEquals(expectedResponse.description(), response.description());

        log.info("Verifications completed. Response: {}", response);
    }

    @Test
    public void testGetChallengeById() {
        int challengeId = 1;

        //Arrange
        ChallengeResponseDTO expectedResponse = new ChallengeResponseDTO(
                1, // id
                1, // creatorUser
                "title",
                "LOSE_WEIGHT", // Use the actual string value of the enum
                "description",
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(1),
                BigDecimal.valueOf(100.0),
                true,
                List.of()
        );
        User mockUser = new User();

        Challenge mockChallenge = new Challenge();
        mockChallenge.setId(1); // Simulate DB-assigned ID

        when(challengeRepository.findById(eq(challengeId))).thenReturn(Optional.of(mockChallenge));
        when(challengeMapper.challengeToChallengeDTO(any(Challenge.class))).thenReturn(expectedResponse);


        //Act
        ChallengeResponseDTO challengeResponseDTO = challengeService.getChallengeById(challengeId);


        //Assert
        verify(challengeRepository).findById(challengeId);
        verify(challengeMapper).challengeToChallengeDTO(mockChallenge);

        assertEquals(challengeResponseDTO.id(), expectedResponse.id());

    }

    @Test
    public void testGetAllChallenges() {
        // Arrange
        Challenge challenge1 = new Challenge();
        challenge1.setId(1);
        challenge1.setPurpose(ChallengePurpose.FITNESS);
        // only for testing purpose I set two fields;
        Challenge challenge2 = new Challenge();
        challenge2.setId(2);
        challenge2.setPurpose(ChallengePurpose.GAIN_WEIGHT);

        List<Challenge> listOfChallenges = List.of(challenge1, challenge2);

        ChallengeResponseDTO challenge1Response = new ChallengeResponseDTO(
                1, // id
                1, // creatorUser
                "title",
                "FITNESS", // Use the actual string value of the enum
                "description",
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(1),
                BigDecimal.valueOf(100.0),
                true,
                List.of()
        );
        ChallengeResponseDTO challenge2Response = new ChallengeResponseDTO(
                2, // id
                1, // creatorUser
                "title",
                "GAIN_WEIGHT",
                "description",
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(1),
                BigDecimal.valueOf(100.0),
                true,
                List.of()
        );


        when(challengeRepository.findAll()).thenReturn(listOfChallenges);
        when(challengeMapper.challengeToChallengeDTO(challenge1)).thenReturn(challenge1Response);
        when(challengeMapper.challengeToChallengeDTO(challenge2)).thenReturn(challenge2Response);

        // Act
        List<ChallengeResponseDTO> result = challengeService.getAllChallenges();


        //Assert

        verify(challengeRepository).findAll();
        verify(challengeMapper).challengeToChallengeDTO(challenge1);
        verify(challengeMapper).challengeToChallengeDTO(challenge1);

        assertEquals(2, result.size());
        assertEquals(challenge1Response.id(), challenge1.getId());
        assertEquals(challenge2Response.id(), challenge2.getId());
    }

    @Test
    public void testDeleteChallengeById() {
        //Arrange
        int challangeId = 1;
        Challenge challenge = new Challenge();
        challenge.setId(challangeId);

        when(challengeRepository.findById(challangeId)).thenReturn(Optional.of(challenge));

        //Act
        challengeService.deleteChallengeById(challangeId);

        //Assert
        verify(challengeRepository).delete(challenge);
    }

    @Test
    void testUpdateChallenge() {
        // Arrange
        int challengeId = 1;
        ChallengeUpdateRequestDTO challengeDTO = new ChallengeUpdateRequestDTO(
                1,
                "title",
                "description",
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(1),
                BigDecimal.valueOf(100.0),
                true
        );
        User mockUser = new User();
        mockUser.setId(1);

        Challenge challenge = new Challenge();
        challenge.setId(1); // Simulate DB-assigned ID

        ChallengeResponseDTO expectedResponse = new ChallengeResponseDTO(
                1, // id
                1, // creatorUser
                "title",
                "LOSE_WEIGHT", // Use the actual string value of the enum
                "description",
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(1),
                BigDecimal.valueOf(100.0),
                true,
                List.of()
        );

        when(challengeRepository.findById(challengeId)).thenReturn(Optional.of(challenge));
        when(challengeRepository.save(challenge)).thenReturn(challenge);
        when(challengeMapper.challengeToChallengeDTO(challenge)).thenReturn(expectedResponse);

        // Act
        ChallengeResponseDTO result = challengeService.updateChallenge(challengeId, challengeDTO);

        // Assert
        verify(challengeRepository).findById(challengeId);
        verify(challengeRepository).save(challenge);
        verify(challengeMapper).challengeToChallengeDTO(challenge);

        assertEquals(expectedResponse, result);
    }

    @Test
    public void testUserIsParticipant() {
        int challengeId = 1;
        int userId = 42;
        //Arrange
        Challenge challenge = new Challenge();
        challenge.setId(challengeId);

        ChallengeParticipant participant = new ChallengeParticipant();
        User user = new User();
        user.setId(userId);
        participant.setUser(user);
        challenge.setChallengeParticipants(Set.of(participant));
        when(challengeRepository.findById(challengeId)).thenReturn(Optional.of(challenge));

        // Act
        boolean result = challengeService.userIsParticipant(challengeId, userId);

        // Assert
        assertTrue(result, "User should be recognized as a participant");
    }

}
