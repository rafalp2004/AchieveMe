package com.achiveme.mvp.mapper;

import com.achiveme.mvp.dto.challenge.ChallengeResponseDTO;
import com.achiveme.mvp.entity.Challenge;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {ParticipantMapper.class})
public interface ChallengeMapper {
    @Mapping(source = "creatorUser.id", target = "creatorUser")
    @Mapping(source ="challengeParticipants", target = "participants")
    ChallengeResponseDTO challengeToChallengeDTO(Challenge challenge);
}
