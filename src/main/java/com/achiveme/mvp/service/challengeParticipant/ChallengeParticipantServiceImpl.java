package com.achiveme.mvp.service.challengeParticipant;

import com.achiveme.mvp.dto.challengeParticipant.ChallengeParticipantResponseDTO;
import com.achiveme.mvp.dto.challengeParticipant.ChallengeParticipantUpdateDTO;
import com.achiveme.mvp.entity.Challenge;
import com.achiveme.mvp.entity.ChallengeParticipant;
import com.achiveme.mvp.entity.User;
import com.achiveme.mvp.enums.ParticipantStatus;
import com.achiveme.mvp.enums.PaymentStatus;
import com.achiveme.mvp.exception.Challenge.ChallengeDoesNotExistException;
import com.achiveme.mvp.exception.ChallengeParticipant.ChallengeParticipantDoesNotExistException;
import com.achiveme.mvp.exception.User.UserAlreadyExistsInChallengeException;
import com.achiveme.mvp.mapper.ParticipantMapper;
import com.achiveme.mvp.repository.ChallengeParticipantRepository;
import com.achiveme.mvp.repository.ChallengeRepository;
import com.achiveme.mvp.repository.UserRepository;
import com.achiveme.mvp.service.user.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChallengeParticipantServiceImpl implements ChallengeParticipantService {
    private final ChallengeParticipantRepository challengeParticipantRepository;
    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final ParticipantMapper participantMapper;

    private final UserService userService;

    public ChallengeParticipantServiceImpl(ChallengeParticipantRepository challengeParticipantRepository, UserRepository userRepository, ChallengeRepository challengeRepository, ParticipantMapper participantMapper, UserService userService) {
        this.challengeParticipantRepository = challengeParticipantRepository;
        this.userRepository = userRepository;
        this.challengeRepository = challengeRepository;
        this.participantMapper = participantMapper;
        this.userService = userService;
    }

    @Override
    public ChallengeParticipantResponseDTO createParticipant(int challengeId) {

        User user = userService.getCurrentUser();
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(() -> new ChallengeDoesNotExistException("Challenge with id: " + challengeId + " does not exist"));
        if (challenge.getChallengeParticipants().stream().anyMatch(s -> s.getUser().equals(user))) {
            throw new UserAlreadyExistsInChallengeException("User " + user.getId() + " already exists in challenge");
        }
        ChallengeParticipant challengeParticipant = new ChallengeParticipant();
        challengeParticipant.setChallenge(challenge);
        challengeParticipant.setUser(user);
        challengeParticipant.setPaymentStatus(PaymentStatus.SUCCEEDED);
        challengeParticipant.setParticipantStatus(ParticipantStatus.ACTIVE);
        challengeParticipant.setJoinDate(LocalDateTime.now());

        challenge.addParticipant(challengeParticipant);
        challengeParticipantRepository.save(challengeParticipant);
        return participantMapper.participantToParticipantDTO(challengeParticipant);
    }

    @Override
    public List<ChallengeParticipantResponseDTO> getParticipantsByChallengeId(int id) {
        Challenge challenge = challengeRepository.findById(id).orElseThrow(() -> new ChallengeDoesNotExistException("Challenge with id: " + id + " do not exist"));
        return challenge.getChallengeParticipants().stream().map(participantMapper::participantToParticipantDTO).toList();
    }

    @Override
    public void deleteParticipantById(int challengeId, int participantId) {

        ChallengeParticipant challengeParticipant = challengeParticipantRepository.findById(participantId).orElseThrow(() -> new ChallengeParticipantDoesNotExistException("Challenge participant with id: " + participantId + " does not exist"));
        challengeParticipantRepository.delete(challengeParticipant);
    }

    @Override
    public ChallengeParticipantResponseDTO updateUserStatus(int challengeId, int participantId, ChallengeParticipantUpdateDTO participantUpdateDTO) {
        ChallengeParticipant participant = challengeParticipantRepository.findById(participantId).orElseThrow(() -> new ChallengeParticipantDoesNotExistException("Challenge participant with id: " + participantId + " does not exist"));
        if (participantUpdateDTO.participantStatus() != null) {
            participant.setParticipantStatus(participantUpdateDTO.participantStatus());
        }
        if (participantUpdateDTO.paymentStatus() != null) {
            participant.setPaymentStatus(participantUpdateDTO.paymentStatus());
        }
        challengeParticipantRepository.save(participant);
        return participantMapper.participantToParticipantDTO(participant);
    }
}
