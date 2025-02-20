package com.achiveme.mvp.service.checkPosts;

import com.achiveme.mvp.dto.checkPost.CheckPostRequestDTO;
import com.achiveme.mvp.dto.checkPost.CheckPostResponseDTO;
import com.achiveme.mvp.dto.checkPost.CheckPostUpdateRequestDTO;
import com.achiveme.mvp.entity.Challenge;
import com.achiveme.mvp.entity.CheckPost;
import com.achiveme.mvp.entity.User;
import com.achiveme.mvp.enums.CheckPostStatus;
import com.achiveme.mvp.exception.Challenge.ChallengeDoesNotExistException;
import com.achiveme.mvp.exception.CheckPost.CheckPostDoesNotExistException;
import com.achiveme.mvp.exception.User.UnauthorizedException;
import com.achiveme.mvp.mapper.CheckPostMapper;
import com.achiveme.mvp.repository.ChallengeRepository;
import com.achiveme.mvp.repository.CheckPostRepository;
import com.achiveme.mvp.service.challenge.ChallengeService;
import com.achiveme.mvp.service.challenge.ChallengeServiceImpl;
import com.achiveme.mvp.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class CheckPostServiceImpl implements CheckPostService {

    private final CheckPostRepository checkPostRepository;
    private final ChallengeRepository challengeRepository;
    private final UserService userService;

    private final CheckPostMapper checkPostMapper;
    private final ChallengeService challengeService;

    public CheckPostServiceImpl(CheckPostRepository checkPostRepository, ChallengeRepository challengeRepository, UserService userService, CheckPostMapper checkPostMapper, ChallengeServiceImpl challengeService) {
        this.checkPostRepository = checkPostRepository;
        this.challengeRepository = challengeRepository;
        this.userService = userService;
        this.checkPostMapper = checkPostMapper;
        this.challengeService = challengeService;
    }

    @Override
    public CheckPostResponseDTO createCheckPost(int challengeId, CheckPostRequestDTO checkPostDTO) {
        User user = userService.getCurrentUser();
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(() -> new ChallengeDoesNotExistException("Challenge with id: " + challengeId + " does not exist"));
        //Check if user has active participant status in challenge
        log.info(challenge.getChallengeParticipants().toString());
        if (challengeService.userIsParticipant(challengeId, user.getId())) {
            CheckPost checkPost = new CheckPost();
            checkPost.setUser(user);
            checkPost.setChallenge(challenge);
            checkPost.setTitle(checkPostDTO.title());
            checkPost.setDescription(checkPostDTO.description());
            checkPost.setPublishedDate(LocalDateTime.now());
            checkPost.setCheckPostStatus(CheckPostStatus.UNCONFIRMED);

            checkPostRepository.save(checkPost);

            return checkPostMapper.checkPostToCheckPostDTO(checkPost);
        } else
            throw new UnauthorizedException("User with id " + user.getId() + " is not a part of challenge with id " + challengeId);
    }

    @Override
    public List<CheckPostResponseDTO> getAllChallengeCheckPosts(int challengeId) {
        User user = userService.getCurrentUser();
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(() -> new ChallengeDoesNotExistException("Challenge with id: " + challengeId + " does not exist"));
        if (challengeService.userIsParticipant(challengeId, user.getId()) || userService.isCurrentUserAdmin()) {
            return challenge.getCheckPosts().stream().map(checkPostMapper::checkPostToCheckPostDTO).toList();
        } else
            throw new UnauthorizedException("User with id " + user.getId() + " is not a part of challenge with id " + challengeId);

    }


    @Override
    public CheckPostResponseDTO getCheckPost(int id) {
        CheckPost checkPost = checkPostRepository.findById(id).orElseThrow(() -> new CheckPostDoesNotExistException("Check post with id: " + id + " does not exist"));
        return checkPostMapper.checkPostToCheckPostDTO(checkPost);
    }

    @Override
    public void deleteCheckPost(int id) {
        checkPostRepository.deleteById(id);
    }

    @Override
    public void confirmCheckPost(int checkPostId) {
        CheckPost checkPost = checkPostRepository.findById(checkPostId).orElseThrow(() -> new CheckPostDoesNotExistException("Check post with id: " + checkPostId + " does not exist"));
        User user = userService.getCurrentUser();
        if (user.getRoles().stream().anyMatch(role -> (role.getName().equals("ROLE_MODERATOR") || role.getName().equals("ROLE_ADMIN")))) {
            checkPost.setCheckPostStatus(CheckPostStatus.CONFIRMED);
            checkPostRepository.save(checkPost);
        } else {
            throw new UnauthorizedException("Only moderators and admins can confirm post");
        }
    }

    @Override
    public void unconfirmCheckPost(int checkPostId) {
        CheckPost checkPost = checkPostRepository.findById(checkPostId).orElseThrow(() -> new CheckPostDoesNotExistException("Check post with id: " + checkPostId + " does not exist"));
        User user = userService.getCurrentUser();
        if (user.getRoles().stream().anyMatch(role -> (role.getName().equals("ROLE_MODERATOR") || role.getName().equals("ROLE_ADMIN")))) {
            checkPost.setCheckPostStatus(CheckPostStatus.UNCONFIRMED);
            checkPostRepository.save(checkPost);
        } else {
            throw new UnauthorizedException("Only moderators and admins can unconfirm post");
        }
    }

    @Override
    public void updateCheckPost(int checkPostId, CheckPostUpdateRequestDTO checkPostUpdateRequestDTO) {
        CheckPost checkPost = checkPostRepository.findById(checkPostId).orElseThrow(() -> new CheckPostDoesNotExistException("Check post with id: " + checkPostId + " does not exist"));
        checkPost.setTitle(checkPostUpdateRequestDTO.title());
        checkPost.setDescription(checkPostUpdateRequestDTO.description());
        checkPostRepository.save(checkPost);
    }
}
