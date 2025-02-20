package com.achiveme.mvp.mapper;

import com.achiveme.mvp.dto.challenge.ChallengeResponseDTO;
import com.achiveme.mvp.dto.challengeParticipant.ChallengeParticipantResponseDTO;
import com.achiveme.mvp.entity.Challenge;
import com.achiveme.mvp.entity.ChallengeParticipant;
import com.achiveme.mvp.entity.User;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-16T17:08:07+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.7 (Oracle Corporation)"
)
@Component
public class ChallengeMapperImpl implements ChallengeMapper {

    @Autowired
    private ParticipantMapper participantMapper;

    @Override
    public ChallengeResponseDTO challengeToChallengeDTO(Challenge challenge) {
        if ( challenge == null ) {
            return null;
        }

        int creatorUser = 0;
        List<ChallengeParticipantResponseDTO> participants = null;
        int id = 0;
        String title = null;
        String purpose = null;
        String description = null;
        LocalDateTime deadline = null;
        LocalDateTime startAt = null;
        BigDecimal entryFee = null;
        boolean isPublic = false;

        creatorUser = challengeCreatorUserId( challenge );
        participants = challengeParticipantSetToChallengeParticipantResponseDTOList( challenge.getChallengeParticipants() );
        id = challenge.getId();
        title = challenge.getTitle();
        if ( challenge.getPurpose() != null ) {
            purpose = challenge.getPurpose().name();
        }
        description = challenge.getDescription();
        deadline = challenge.getDeadline();
        startAt = challenge.getStartAt();
        entryFee = challenge.getEntryFee();
        if ( challenge.getIsPublic() != null ) {
            isPublic = challenge.getIsPublic();
        }

        ChallengeResponseDTO challengeResponseDTO = new ChallengeResponseDTO( id, creatorUser, title, purpose, description, deadline, startAt, entryFee, isPublic, participants );

        return challengeResponseDTO;
    }

    private int challengeCreatorUserId(Challenge challenge) {
        if ( challenge == null ) {
            return 0;
        }
        User creatorUser = challenge.getCreatorUser();
        if ( creatorUser == null ) {
            return 0;
        }
        int id = creatorUser.getId();
        return id;
    }

    protected List<ChallengeParticipantResponseDTO> challengeParticipantSetToChallengeParticipantResponseDTOList(Set<ChallengeParticipant> set) {
        if ( set == null ) {
            return null;
        }

        List<ChallengeParticipantResponseDTO> list = new ArrayList<ChallengeParticipantResponseDTO>( set.size() );
        for ( ChallengeParticipant challengeParticipant : set ) {
            list.add( participantMapper.participantToParticipantDTO( challengeParticipant ) );
        }

        return list;
    }
}
