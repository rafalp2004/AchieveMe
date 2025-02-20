package com.achiveme.mvp.mapper;

import com.achiveme.mvp.dto.challengeParticipant.ChallengeParticipantResponseDTO;
import com.achiveme.mvp.entity.Challenge;
import com.achiveme.mvp.entity.ChallengeParticipant;
import com.achiveme.mvp.entity.User;
import com.achiveme.mvp.enums.ParticipantStatus;
import com.achiveme.mvp.enums.PaymentStatus;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-16T17:08:07+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.7 (Oracle Corporation)"
)
@Component
public class ParticipantMapperImpl implements ParticipantMapper {

    @Override
    public ChallengeParticipantResponseDTO participantToParticipantDTO(ChallengeParticipant participant) {
        if ( participant == null ) {
            return null;
        }

        int challengeId = 0;
        int userId = 0;
        int id = 0;
        LocalDateTime joinDate = null;
        ParticipantStatus participantStatus = null;
        PaymentStatus paymentStatus = null;

        challengeId = participantChallengeId( participant );
        userId = participantUserId( participant );
        id = participant.getId();
        joinDate = participant.getJoinDate();
        participantStatus = participant.getParticipantStatus();
        paymentStatus = participant.getPaymentStatus();

        ChallengeParticipantResponseDTO challengeParticipantResponseDTO = new ChallengeParticipantResponseDTO( id, challengeId, userId, joinDate, participantStatus, paymentStatus );

        return challengeParticipantResponseDTO;
    }

    private int participantChallengeId(ChallengeParticipant challengeParticipant) {
        if ( challengeParticipant == null ) {
            return 0;
        }
        Challenge challenge = challengeParticipant.getChallenge();
        if ( challenge == null ) {
            return 0;
        }
        int id = challenge.getId();
        return id;
    }

    private int participantUserId(ChallengeParticipant challengeParticipant) {
        if ( challengeParticipant == null ) {
            return 0;
        }
        User user = challengeParticipant.getUser();
        if ( user == null ) {
            return 0;
        }
        int id = user.getId();
        return id;
    }
}
