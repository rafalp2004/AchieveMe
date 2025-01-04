package com.achiveme.mvp.mapper;

import com.achiveme.mvp.dto.challengeParticipant.ChallengeParticipantResponseDTO;
import com.achiveme.mvp.entity.ChallengeParticipant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {
    @Mapping(source="challenge.id", target = "challengeId")
    @Mapping(source="user.id", target="userId")
    ChallengeParticipantResponseDTO participantToParticipantDTO(ChallengeParticipant participant);

}
