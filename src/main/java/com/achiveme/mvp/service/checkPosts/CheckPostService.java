package com.achiveme.mvp.service.checkPosts;

import com.achiveme.mvp.dto.checkPost.CheckPostResponseDTO;
import com.achiveme.mvp.dto.checkPost.CheckPostRequestDTO;
import com.achiveme.mvp.dto.checkPost.CheckPostUpdateRequestDTO;

import java.util.List;

public interface CheckPostService {
    CheckPostResponseDTO createCheckPost(int challengeId, CheckPostRequestDTO checkPostDTO);
    CheckPostResponseDTO getCheckPost(int id);
    void deleteCheckPost(int id);
    void confirmCheckPost(int checkPostId);

    void unconfirmCheckPost(int checkPostId);

    void updateCheckPost(int checkPostId, CheckPostUpdateRequestDTO checkPostUpdateRequestDTO);

    List<CheckPostResponseDTO> getAllChallengeCheckPosts(int challengeId);
}
