package com.achiveme.mvp.controller;

import com.achiveme.mvp.dto.roadmap.RoadmapInfoDTO;
import com.achiveme.mvp.dto.roadmap.RoadmapResponseDTO;
import com.achiveme.mvp.dto.roadmap.RoadmapUpdateDTO;
import com.achiveme.mvp.service.roadmap.RoadmapService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoadmapAIController {

    private final RoadmapService roadMapService;

    public RoadmapAIController(RoadmapService roadMapService) {
        this.roadMapService = roadMapService;
    }


    @PostMapping("/roadmap")
    public ResponseEntity<RoadmapResponseDTO> createRoadmap(@RequestBody RoadmapInfoDTO roadmapDTO){
        RoadmapResponseDTO response = roadMapService.createRoadmap(roadmapDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/roadmap")
    public ResponseEntity<RoadmapResponseDTO> getRoadmap(@RequestParam int userId, @RequestParam int challengeId){
        RoadmapResponseDTO roadmap = roadMapService.getRoadmap(userId, challengeId);
        return new ResponseEntity<>(roadmap, HttpStatus.OK);
    }
    @PatchMapping("/roadmap")
    public ResponseEntity<RoadmapResponseDTO> updateRoadmap(@RequestBody RoadmapUpdateDTO roadmapUpdateDTO){
        RoadmapResponseDTO roadmap = roadMapService.update(roadmapUpdateDTO);
        return new ResponseEntity<>(roadmap, HttpStatus.OK);
    }


}
