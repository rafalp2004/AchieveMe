package com.achiveme.mvp.service.challenge;

import com.achiveme.mvp.dto.challenge.ChallengeCreateRequestDTO;
import com.achiveme.mvp.dto.challenge.ChallengeResponseDTO;
import com.achiveme.mvp.dto.challenge.ChallengeUpdateRequestDTO;
import com.achiveme.mvp.entity.Challenge;
import com.achiveme.mvp.exception.Challenge.ChallengeDoesNotExistException;
import com.achiveme.mvp.mapper.ChallengeMapper;
import com.achiveme.mvp.repository.ChallengeRepository;
import com.achiveme.mvp.repository.UserRepository;
import com.achiveme.mvp.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ChallengeServiceImpl implements ChallengeService {

    private final UserRepository userRepository;
    private final ChallengeMapper challengeMapper;
    private final ChallengeRepository challengeRepository;
    private final UserService userService;

    public ChallengeServiceImpl(UserService userService, UserRepository userRepository, ChallengeMapper challengeMapper, ChallengeRepository challengeRepository, UserService userService1) {
        this.userRepository = userRepository;
        this.challengeMapper = challengeMapper;
        this.challengeRepository = challengeRepository;
        this.userService = userService1;
    }


    @Override
    public ChallengeResponseDTO createChallenge(ChallengeCreateRequestDTO challengeDTO) {
        Challenge challenge = new Challenge();

        challenge.setCreatorUser(userService.getCurrentUser());
        challenge.setTitle(challengeDTO.title());
        challenge.setPurpose(challengeDTO.purpose());
        challenge.setDescription(challengeDTO.description());
        challenge.setDeadline(challengeDTO.deadline());
        challenge.setEntryFee(challengeDTO.entryFee());
        challenge.setIsPublic(challengeDTO.isPublic());
        challenge.setCreatedAt(LocalDateTime.now());
        challenge.setStartAt(challengeDTO.startAt());
        challengeRepository.save(challenge);
        return challengeMapper.challengeToChallengeDTO(challenge);
    }

    @Override
    public ChallengeResponseDTO getChallengeById(int id) {
        Challenge challenge = challengeRepository.findById(id).orElseThrow(() -> new ChallengeDoesNotExistException("Challenge with id: " + id + " does not exist"));
        return challengeMapper.challengeToChallengeDTO(challenge);
    }

    @Override
    public List<ChallengeResponseDTO> getAllChallenges() {
        return challengeRepository.findAll().stream().map(challengeMapper::challengeToChallengeDTO).toList();
    }

    @Override
    public void deleteChallengeById(int id) {
        Challenge challenge = challengeRepository.findById(id).orElseThrow(() -> new ChallengeDoesNotExistException("Challenge with id: +" + id + " does not exist"));
        challengeRepository.delete(challenge);
    }

    @Override
    public ChallengeResponseDTO updateChallenge(int id, ChallengeUpdateRequestDTO challengeDTO) {
        Challenge challenge = challengeRepository.findById(id).orElseThrow(() -> new ChallengeDoesNotExistException("Challenge with id: +" + id + " does not exist"));
        challenge.setTitle(challengeDTO.title());
        challenge.setDescription(challengeDTO.description());
        challenge.setDeadline(challengeDTO.deadline());
        challenge.setEntryFee(challengeDTO.entryFee());
        challenge.setIsPublic(challengeDTO.isPublic());

        challengeRepository.save(challenge);

        return challengeMapper.challengeToChallengeDTO(challenge);
    }

    @Override
    public boolean userIsParticipant(int challengeId, int userId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(() -> new ChallengeDoesNotExistException("Challenge with id: +" + challengeId + " does not exist"));
        return (challenge.getChallengeParticipants().stream().anyMatch(participant -> participant.getUser().getId() == userId));
    }
}
