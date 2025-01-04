package com.achiveme.mvp.controller;

import com.achiveme.mvp.dto.challenge.ChallengeCreateRequestDTO;
import com.achiveme.mvp.dto.challenge.ChallengeResponseDTO;
import com.achiveme.mvp.dto.challenge.ChallengeUpdateRequestDTO;
import com.achiveme.mvp.service.challenge.ChallengeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ChallengeController {
    private final ChallengeService challengeService;

    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @GetMapping("/challenges/{id}")
    public ResponseEntity<ChallengeResponseDTO> getChallengeById( @PathVariable int id){
        ChallengeResponseDTO challengeResponseDTO = challengeService.getChallengeById(id);
        return new ResponseEntity<>(challengeResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/challenges")
    public ResponseEntity<List<ChallengeResponseDTO>> getChallenges(){
        return new ResponseEntity<>(challengeService.getAllChallenges(), HttpStatus.OK);
    }


    @PostMapping("/challenges")
    ResponseEntity<ChallengeResponseDTO> createChallenge(@Valid @RequestBody ChallengeCreateRequestDTO challengeDTO){
        ChallengeResponseDTO challenge = challengeService.createChallenge(challengeDTO);
        return new ResponseEntity<>(challenge, HttpStatus.CREATED);
    }

    @PutMapping("/challenges/{id}")
    ResponseEntity<ChallengeResponseDTO> updateChallenge(
            @PathVariable int id ,
            @Valid @RequestBody ChallengeUpdateRequestDTO challengeDTO){
        ChallengeResponseDTO challenge = challengeService.updateChallenge(id,challengeDTO);
        return new ResponseEntity<>(challenge, HttpStatus.OK);
    }

    @DeleteMapping("/challenges/{id}")
    public ResponseEntity<Void> deleteChallengeById(@PathVariable int id){
        challengeService.deleteChallengeById(id);
        return ResponseEntity.noContent().build();
    }

}
