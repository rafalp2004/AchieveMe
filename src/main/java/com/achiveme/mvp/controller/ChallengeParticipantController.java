package com.achiveme.mvp.controller;

import com.achiveme.mvp.dto.challengeParticipant.ChallengeParticipantResponseDTO;
import com.achiveme.mvp.dto.challengeParticipant.ChallengeParticipantUpdateDTO;
import com.achiveme.mvp.service.challengeParticipant.ChallengeParticipantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
public class ChallengeParticipantController {

    private final ChallengeParticipantService challengeParticipantService;

    public ChallengeParticipantController(ChallengeParticipantService challengeParticipantService) {
        this.challengeParticipantService = challengeParticipantService;
    }


    @PostMapping("/challenges/{challengeID}/participants")
    ResponseEntity<ChallengeParticipantResponseDTO> createChallengeParticipant(
            @PathVariable int challengeID) {
        ChallengeParticipantResponseDTO createdParticipant = challengeParticipantService.createParticipant(challengeID);
        return ResponseEntity
                .created(URI.create("/challenges/" + challengeID + "/participants/" + createdParticipant.id()))
                .body(createdParticipant);
    }

    @GetMapping("/challenges/{challengeId}/participants")
    ResponseEntity<List<ChallengeParticipantResponseDTO>> getChallengeParticipants(@PathVariable int challengeId) {
        return new ResponseEntity<>(challengeParticipantService.getParticipantsByChallengeId(challengeId), HttpStatus.OK);
    }

    @DeleteMapping("/challenges/{challengeId}/participants/{participantId}")
    ResponseEntity<Void> deleteChallengeParticipant(
            @PathVariable int challengeId,
            @PathVariable int participantId) {
        challengeParticipantService.deleteParticipantById(challengeId, participantId);
        return ResponseEntity.noContent().build();
    }

    //TODO later add validation
    @PatchMapping("/challenges/{challengeId}/participants/{participantId}")
    ResponseEntity<ChallengeParticipantResponseDTO> updateStatusChallengeParticipant(
            @PathVariable int challengeId,
            @PathVariable int participantId,
            @RequestBody ChallengeParticipantUpdateDTO participantUpdateDTO) {
        ChallengeParticipantResponseDTO participantResponseDTO = challengeParticipantService.updateUserStatus(challengeId, participantId, participantUpdateDTO);
        return new ResponseEntity<>(participantResponseDTO, HttpStatus.OK);
    }
}
