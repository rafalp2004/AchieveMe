package com.achiveme.mvp.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.achiveme.mvp.dto.checkPost.CheckPostRequestDTO;
import com.achiveme.mvp.dto.checkPost.CheckPostResponseDTO;
import com.achiveme.mvp.entity.*;
import com.achiveme.mvp.enums.CheckPostStatus;
import com.achiveme.mvp.exception.User.UnauthorizedException;
import com.achiveme.mvp.mapper.CheckPostMapper;
import com.achiveme.mvp.repository.ChallengeRepository;
import com.achiveme.mvp.repository.CheckPostRepository;
import com.achiveme.mvp.service.challenge.ChallengeService;
import com.achiveme.mvp.service.challenge.ChallengeServiceImpl;
import com.achiveme.mvp.service.checkPosts.CheckPostService;
import com.achiveme.mvp.service.checkPosts.CheckPostServiceImpl;
import com.achiveme.mvp.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CheckPostServiceTest {

    @Mock
    private CheckPostRepository checkPostRepository;

    @Mock
    private ChallengeRepository challengeRepository;

    @Mock
    private UserService userService;

    @Mock
    private CheckPostMapper checkPostMapper;

    @Mock
    private ChallengeServiceImpl challengeService;

    @InjectMocks
    private CheckPostServiceImpl checkPostService;

    @Test
    public void createCheckPost_userIsParticipant_returnsCheckPostDTO() {

        // Arrange
        int challengeId = 1;
        CheckPostRequestDTO checkPostDTO = new CheckPostRequestDTO("Check Title", "Check description");

        User currentUser = new User();
        currentUser.setId(100);

        Challenge challenge = new Challenge();
        challenge.setId(challengeId);
        // Assume challenge has participants already populated.

        ChallengeParticipant participant = new ChallengeParticipant();
        User participantUser = new User();
        participantUser.setId(100); // same as current user
        participant.setUser(participantUser);
        challenge.setChallengeParticipants(Set.of(participant));

        CheckPost checkPost = new CheckPost();
        checkPost.setUser(currentUser);
        checkPost.setChallenge(challenge);
        checkPost.setTitle(checkPostDTO.title());
        checkPost.setDescription(checkPostDTO.description());
        checkPost.setPublishedDate(LocalDateTime.now());
        checkPost.setCheckPostStatus(CheckPostStatus.UNCONFIRMED);

        CheckPostResponseDTO expectedResponse = new CheckPostResponseDTO(
                1, // id assigned by repository, for example
                challengeId,
                checkPostDTO.title(),
                checkPostDTO.description(),// published date (not asserting exact value)
                CheckPostStatus.UNCONFIRMED,
                LocalDateTime.now()
                );

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(challengeRepository.findById(challengeId)).thenReturn(Optional.of(challenge));
        when(challengeService.userIsParticipant(challengeId, currentUser.getId())).thenReturn(true);
        when(checkPostRepository.save(any(CheckPost.class))).thenReturn(checkPost);
        when(checkPostMapper.checkPostToCheckPostDTO(any(CheckPost.class))).thenReturn(expectedResponse);

        // Act
        CheckPostResponseDTO response = checkPostService.createCheckPost(challengeId, checkPostDTO);

        // Assert
        // Verify that each dependency was invoked correctly.
        verify(userService).getCurrentUser();
        verify(challengeRepository).findById(challengeId);
        verify(challengeService).userIsParticipant(challengeId, currentUser.getId());
        verify(checkPostRepository).save(any(CheckPost.class));
        verify(checkPostMapper).checkPostToCheckPostDTO(any(CheckPost.class));

        assertNotNull(response);
        assertEquals(expectedResponse.id(), response.id() );
        assertEquals(expectedResponse.title(), response.title());
    }

    @Test
    public void createCheckPost_userNotParticipant_throwsUnauthorizedException() {

        // Arrange
        int challengeId = 1;
        CheckPostRequestDTO checkPostDTO = new CheckPostRequestDTO("Check Title", "Check description");
        User currentUser = new User();
        currentUser.setId(100);
        Challenge challenge = new Challenge();
        challenge.setId(challengeId);
        challenge.setChallengeParticipants(Set.of());

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(challengeRepository.findById(challengeId)).thenReturn(Optional.of(challenge));
        when(challengeService.userIsParticipant(challengeId, currentUser.getId())).thenReturn(false);

        // Act & Assert
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            checkPostService.createCheckPost(challengeId, checkPostDTO);
        });
        assertTrue(exception.getMessage().contains("is not a part of challenge"));
    }


    @Test
    public void getAllChallengeCheckPosts_userIsParticipantOrAdmin_returnsCheckPosts() {
        // Arrange
        int challengeId = 1;
        User currentUser = new User();
        currentUser.setId(100);

        Challenge challenge = new Challenge();
        challenge.setId(challengeId);

        CheckPost post1 = new CheckPost();
        CheckPost post2 = new CheckPost();
        challenge.setCheckPosts(Set.of(post1, post2));


        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(challengeRepository.findById(challengeId)).thenReturn(Optional.of(challenge));
        when(challengeService.userIsParticipant(challengeId, currentUser.getId())).thenReturn(true);
        when(checkPostMapper.checkPostToCheckPostDTO(any(CheckPost.class))).thenReturn(new CheckPostResponseDTO(1, 1, "Title", "Desc", CheckPostStatus.UNCONFIRMED, LocalDateTime.now()));


        // Act
        List<CheckPostResponseDTO> posts = checkPostService.getAllChallengeCheckPosts(challengeId);

        // Assert
        verify(challengeRepository).findById(challengeId);
        // There should be a mapping call for each check post.
        verify(checkPostMapper).checkPostToCheckPostDTO(post1);
        verify(checkPostMapper).checkPostToCheckPostDTO(post2);
        assertEquals(2, posts.size());
    }


    @Test
    public void getCheckPost_existingCheckPost_returnsDTO() {
        // Arrange
        int checkPostId = 1;
        CheckPost checkPost = new CheckPost();
        checkPost.setId(checkPostId);
        CheckPostResponseDTO expectedDTO = new CheckPostResponseDTO(
                checkPostId,  1, "Title", "Desc", CheckPostStatus.UNCONFIRMED, LocalDateTime.now());

        when(checkPostRepository.findById(checkPostId)).thenReturn(Optional.of(checkPost));
        when(checkPostMapper.checkPostToCheckPostDTO(checkPost)).thenReturn(expectedDTO);

        // Act
        CheckPostResponseDTO result = checkPostService.getCheckPost(checkPostId);

        // Assert
        verify(checkPostRepository).findById(checkPostId);
        verify(checkPostMapper).checkPostToCheckPostDTO(checkPost);
        assertEquals(expectedDTO, result);
    }


    @Test
    public void deleteCheckPost_callsRepositoryDeleteById() {
        int checkPostId = 1;

        // Act
        checkPostService.deleteCheckPost(checkPostId);

        // Assert
        verify(checkPostRepository).deleteById(checkPostId);
    }


    @Test
    public void confirmCheckPost_moderatorConfirms_checkPostStatusUpdated() {
        // Arrange
        int checkPostId = 1;
        CheckPost checkPost = new CheckPost();
        checkPost.setId(checkPostId);
        // Simulate a check post in UNCONFIRMED state.
        checkPost.setCheckPostStatus(CheckPostStatus.UNCONFIRMED);
        User moderator = new User();
        moderator.setId(100);
        // Assume the user has a role of moderator.
        Role modRole = new Role();
        modRole.setName("ROLE_MODERATOR");
        moderator.setRoles(List.of(modRole));

        when(checkPostRepository.findById(checkPostId)).thenReturn(Optional.of(checkPost));
        when(userService.getCurrentUser()).thenReturn(moderator);

        // Act
        checkPostService.confirmCheckPost(checkPostId);

        // Assert
        assertEquals(CheckPostStatus.CONFIRMED, checkPost.getCheckPostStatus());
        verify(checkPostRepository).save(checkPost);
    }

    @Test
    public void confirmCheckPost_nonModerator_throwsUnauthorizedException() {
        // Arrange
        int checkPostId = 1;
        CheckPost checkPost = new CheckPost();
        checkPost.setId(checkPostId);
        checkPost.setCheckPostStatus(CheckPostStatus.UNCONFIRMED);
        User regularUser = new User();
        regularUser.setId(200);
        // Simulate regular user without moderator/admin role.
        regularUser.setRoles(List.of());

        when(checkPostRepository.findById(checkPostId)).thenReturn(Optional.of(checkPost));
        when(userService.getCurrentUser()).thenReturn(regularUser);

        // Act & Assert
        UnauthorizedException ex = assertThrows(UnauthorizedException.class, () -> {
            checkPostService.confirmCheckPost(checkPostId);
        });
        assertTrue(ex.getMessage().contains("Only moderators and admins can confirm post"));
    }


}