package com.achiveme.mvp.service;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.achiveme.mvp.dto.challengeParticipant.ChallengeParticipantResponseDTO;
import com.achiveme.mvp.dto.challengeParticipant.ChallengeParticipantUpdateDTO;
import com.achiveme.mvp.dto.checkPost.CheckPostRequestDTO;
import com.achiveme.mvp.entity.Challenge;
import com.achiveme.mvp.entity.ChallengeParticipant;
import com.achiveme.mvp.entity.User;
import com.achiveme.mvp.enums.ParticipantStatus;
import com.achiveme.mvp.enums.PaymentStatus;
import com.achiveme.mvp.exception.Challenge.ChallengeDoesNotExistException;
import com.achiveme.mvp.exception.User.UserAlreadyExistsInChallengeException;
import com.achiveme.mvp.mapper.ParticipantMapper;
import com.achiveme.mvp.repository.ChallengeParticipantRepository;
import com.achiveme.mvp.repository.ChallengeRepository;
import com.achiveme.mvp.repository.UserRepository;
import com.achiveme.mvp.service.challengeParticipant.ChallengeParticipantServiceImpl;
import com.achiveme.mvp.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ChallengeParticipantServiceTest {

    @Mock
    private ChallengeParticipantRepository challengeParticipantRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChallengeRepository challengeRepository;

    @Mock
    private ParticipantMapper participantMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private ChallengeParticipantServiceImpl challengeParticipantService;

    @Test
    public void createParticipant_success() {
        int challengeId = 1;
        User currentUser = new User();
        currentUser.setId(100);

        // Simulate an existing challenge with no matching participant yet.
        Challenge challenge = new Challenge();
        challenge.setId(challengeId);
        // Initially, challenge has an empty list of participants.
        challenge.setChallengeParticipants(Set.of());

        // Create a dummy participant DTO expected after mapping.
        ChallengeParticipantResponseDTO expectedDTO = new ChallengeParticipantResponseDTO(
                1,  challengeId,currentUser.getId(),  LocalDateTime.now(),
                ParticipantStatus.ACTIVE, PaymentStatus.SUCCEEDED // adjust parameters to match your record's definition
        );

        // Stub the collaborators.
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(challengeRepository.findById(challengeId)).thenReturn(Optional.of(challenge));
        // In the successful path, the user is not already a participant.
        // Assume that the repository saves and returns the created ChallengeParticipant.
        ChallengeParticipant savedParticipant = new ChallengeParticipant();
        savedParticipant.setId(1);
        savedParticipant.setUser(currentUser);
        savedParticipant.setChallenge(challenge);
        savedParticipant.setParticipantStatus(ParticipantStatus.ACTIVE);
        savedParticipant.setPaymentStatus(PaymentStatus.SUCCEEDED);
        savedParticipant.setJoinDate(LocalDateTime.now());
        when(challengeParticipantRepository.save(any(ChallengeParticipant.class))).thenReturn(savedParticipant);
        when(participantMapper.participantToParticipantDTO(any(ChallengeParticipant.class))).thenReturn(expectedDTO);

        // Act
        CheckPostRequestDTO dummyCheckPostDTO = null; // not used in createParticipant
        // In our case, createParticipant only takes the challengeId so no DTO is passed.
        // Calling service method.
        ChallengeParticipantResponseDTO response = challengeParticipantService.createParticipant(challengeId);

        // Assert
        verify(userService).getCurrentUser();
        verify(challengeRepository).findById(challengeId);
        verify(challengeParticipantRepository).save(any(ChallengeParticipant.class));
        verify(participantMapper).participantToParticipantDTO(any(ChallengeParticipant.class));
        assertEquals(expectedDTO, response);
    }

    @Test
    public void createParticipant_alreadyExists_throwsException() {
        int challengeId = 1;
        User currentUser = new User();
        currentUser.setId(100);

        Challenge challenge = new Challenge();
        challenge.setId(challengeId);
        // Populate challenge with an existing participant equal to currentUser.
        ChallengeParticipant existingParticipant = new ChallengeParticipant();
        existingParticipant.setUser(currentUser);
        challenge.setChallengeParticipants(Set.of(existingParticipant));

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(challengeRepository.findById(challengeId)).thenReturn(Optional.of(challenge));

        // Act & Assert
        assertThrows(UserAlreadyExistsInChallengeException.class, () -> {
            challengeParticipantService.createParticipant(challengeId);
        });
    }


    @Test
    public void getParticipantsByChallengeId_success() {
        int challengeId = 1;

        Challenge challenge = new Challenge();
        challenge.setId(challengeId);

        // Prepare two dummy participants.
        ChallengeParticipant participant1 = new ChallengeParticipant();
        participant1.setId(1);
        ChallengeParticipant participant2 = new ChallengeParticipant();
        participant2.setId(2);
        challenge.setChallengeParticipants(Set.of(participant1, participant2));

        // Create dummy DTOs for each participant.
        ChallengeParticipantResponseDTO dto1 = new ChallengeParticipantResponseDTO(
                1, 100, challengeId, LocalDateTime.now(), ParticipantStatus.ACTIVE, PaymentStatus.SUCCEEDED
        );
        ChallengeParticipantResponseDTO dto2 = new ChallengeParticipantResponseDTO(
                2, 101, challengeId, LocalDateTime.now(), ParticipantStatus.ACTIVE, PaymentStatus.SUCCEEDED
        );

        when(challengeRepository.findById(challengeId)).thenReturn(Optional.of(challenge));
        when(participantMapper.participantToParticipantDTO(participant1)).thenReturn(dto1);
        when(participantMapper.participantToParticipantDTO(participant2)).thenReturn(dto2);

        // Act
        List<ChallengeParticipantResponseDTO> responseList = challengeParticipantService.getParticipantsByChallengeId(challengeId);

        // Assert
        verify(challengeRepository).findById(challengeId);
        assertEquals(2, responseList.size());
        assertTrue(responseList.contains(dto1));
        assertTrue(responseList.contains(dto2));
    }

    @Test
    public void getParticipantsByChallengeId_challengeNotFound_throwsException() {
        int challengeId = 1;
        when(challengeRepository.findById(challengeId)).thenReturn(Optional.empty());

        assertThrows(ChallengeDoesNotExistException.class, () -> {
            challengeParticipantService.getParticipantsByChallengeId(challengeId);
        });
    }


    @Test
    public void deleteParticipantById_success() {
        int challengeId = 1;
        int participantId = 10;

        ChallengeParticipant participant = new ChallengeParticipant();
        participant.setId(participantId);
        when(challengeParticipantRepository.findById(participantId)).thenReturn(Optional.of(participant));

        // Act
        challengeParticipantService.deleteParticipantById(challengeId, participantId);

        // Assert
        verify(challengeParticipantRepository).delete(participant);
    }

    @Test
    public void deleteParticipantById_notFound_throwsException() {
        int challengeId = 1;
        int participantId = 10;
        when(challengeParticipantRepository.findById(participantId)).thenReturn(Optional.empty());

        assertThrows(ChallengeDoesNotExistException.class, () -> {
            challengeParticipantService.deleteParticipantById(challengeId, participantId);
        });
    }

    @Test
    public void updateUserStatus_success() {
        int challengeId = 1;
        int participantId = 10;

        ChallengeParticipant participant = new ChallengeParticipant();
        participant.setId(participantId);
        participant.setParticipantStatus(ParticipantStatus.ACTIVE);
        participant.setPaymentStatus(PaymentStatus.SUCCEEDED);

        ChallengeParticipantUpdateDTO updateDTO = new ChallengeParticipantUpdateDTO(
                ParticipantStatus.INACTIVE, PaymentStatus.CANCELED
        );

        when(challengeParticipantRepository.findById(participantId)).thenReturn(Optional.of(participant));
        when(challengeParticipantRepository.save(participant)).thenReturn(participant);

        // Prepare an expected response DTO from the mapper.
        ChallengeParticipantResponseDTO expectedDTO = new ChallengeParticipantResponseDTO(
                participantId, 100, challengeId, LocalDateTime.now(), ParticipantStatus.INACTIVE, PaymentStatus.CANCELED
        );
        when(participantMapper.participantToParticipantDTO(participant)).thenReturn(expectedDTO);

        // Act
        ChallengeParticipantResponseDTO response = challengeParticipantService.updateUserStatus(challengeId, participantId, updateDTO);

        // Assert
        verify(challengeParticipantRepository).findById(participantId);
        verify(challengeParticipantRepository).save(participant);
        verify(participantMapper).participantToParticipantDTO(participant);
        // Check that participant fields are updated.
        assertEquals(ParticipantStatus.INACTIVE, participant.getParticipantStatus());
        assertEquals(PaymentStatus.CANCELED, participant.getPaymentStatus());
        // Also check that the response is as expected.
        assertEquals(expectedDTO, response);
    }
}
