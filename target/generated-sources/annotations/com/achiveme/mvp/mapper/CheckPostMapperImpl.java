package com.achiveme.mvp.mapper;

import com.achiveme.mvp.dto.checkPost.CheckPostResponseDTO;
import com.achiveme.mvp.entity.Challenge;
import com.achiveme.mvp.entity.CheckPost;
import com.achiveme.mvp.enums.CheckPostStatus;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-25T22:48:31+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.7 (Oracle Corporation)"
)
@Component
public class CheckPostMapperImpl implements CheckPostMapper {

    @Override
    public CheckPostResponseDTO checkPostToCheckPostDTO(CheckPost checkPost) {
        if ( checkPost == null ) {
            return null;
        }

        int challengeId = 0;
        int id = 0;
        String title = null;
        String description = null;
        CheckPostStatus checkPostStatus = null;
        LocalDateTime publishedDate = null;

        challengeId = checkPostChallengeId( checkPost );
        id = checkPost.getId();
        title = checkPost.getTitle();
        description = checkPost.getDescription();
        checkPostStatus = checkPost.getCheckPostStatus();
        publishedDate = checkPost.getPublishedDate();

        CheckPostResponseDTO checkPostResponseDTO = new CheckPostResponseDTO( id, challengeId, title, description, checkPostStatus, publishedDate );

        return checkPostResponseDTO;
    }

    private int checkPostChallengeId(CheckPost checkPost) {
        if ( checkPost == null ) {
            return 0;
        }
        Challenge challenge = checkPost.getChallenge();
        if ( challenge == null ) {
            return 0;
        }
        int id = challenge.getId();
        return id;
    }
}
