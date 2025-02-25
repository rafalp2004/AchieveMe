package com.achiveme.mvp.mapper;

import com.achiveme.mvp.dto.challengeParticipant.ChallengeParticipantResponseDTO;
import com.achiveme.mvp.dto.roadmap.RoadmapResponseDTO;
import com.achiveme.mvp.entity.ChallengeParticipant;
import com.achiveme.mvp.entity.Roadmap;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface RoadmapMapper {
    @Mapping(source = "challenge.id", target = "challengeId")
    @Mapping(source = "user.id", target = "userId")
    RoadmapResponseDTO roadmapToRoadmapDTO(Roadmap roadmap);
}
