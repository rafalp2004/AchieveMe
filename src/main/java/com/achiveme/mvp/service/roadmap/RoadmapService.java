package com.achiveme.mvp.service.roadmap;

import com.achiveme.mvp.dto.roadmap.RoadmapInfoDTO;
import com.achiveme.mvp.dto.roadmap.RoadmapResponseDTO;
import com.achiveme.mvp.dto.roadmap.RoadmapUpdateDTO;

public interface RoadmapService {
    RoadmapResponseDTO createRoadmap(RoadmapInfoDTO roadmapDTO);
    RoadmapResponseDTO getRoadmap(int userId, int challengeId);


    RoadmapResponseDTO update(RoadmapUpdateDTO roadmapUpdateDTO);
}
