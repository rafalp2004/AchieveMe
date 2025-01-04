package com.achiveme.mvp.controller;

import com.achiveme.mvp.dto.checkPost.CheckPostResponseDTO;
import com.achiveme.mvp.dto.checkPost.CheckPostRequestDTO;
import com.achiveme.mvp.dto.checkPost.CheckPostUpdateRequestDTO;
import com.achiveme.mvp.service.checkPosts.CheckPostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CheckPostController {
    private final CheckPostService checkPostService;

    public CheckPostController(CheckPostService checkPostService) {
        this.checkPostService = checkPostService;
    }

    @PostMapping(path= "/challenges/{challengeId}/check-posts")
    public ResponseEntity<CheckPostResponseDTO> createCheckPost(@PathVariable int challengeId, @Valid @RequestBody CheckPostRequestDTO checkPostDTO){
        CheckPostResponseDTO checkPostResponseDTO = checkPostService.createCheckPost(challengeId, checkPostDTO);
        return new ResponseEntity<>(checkPostResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping(path= "/challenges/{challengeId}/check-posts")
    public ResponseEntity<List<CheckPostResponseDTO>> getAllCheckPosts(@PathVariable int challengeId){
        List<CheckPostResponseDTO> checkPosts = checkPostService.getAllChallengeCheckPosts(challengeId);
        return new ResponseEntity<>(checkPosts, HttpStatus.OK);
    }

    @GetMapping(path="/challenges/check-posts/{id}")
    public ResponseEntity<CheckPostResponseDTO> getCheckPostById(@PathVariable int id){
        CheckPostResponseDTO checkPost = checkPostService.getCheckPost(id);
        return new ResponseEntity<>(checkPost, HttpStatus.OK);
    }
    @DeleteMapping(path="/challenges/check-posts/{id}")
    public ResponseEntity<Void> deleteCheckPostById(@PathVariable int id){
        checkPostService.deleteCheckPost(id);
        return ResponseEntity.noContent().build();

    }

    @PutMapping(path= "/challenges/check-posts/{checkPostId}")
    public ResponseEntity<Void> updateCheckPost(@PathVariable int checkPostId, @Valid @RequestBody CheckPostUpdateRequestDTO checkPostUpdateRequestDTO){
        checkPostService.updateCheckPost(checkPostId, checkPostUpdateRequestDTO);
        return ResponseEntity.noContent().build();
    }   


    @PostMapping(path= "/challenges/check-posts/{checkPostId}/confirm")
    public ResponseEntity<Void> confirmCheckPost(@PathVariable int checkPostId){
        checkPostService.confirmCheckPost(checkPostId);
        return ResponseEntity.noContent().build();
    }
    @PostMapping(path="/challenges/check-posts/{checkPostId}/unconfirm")
    public ResponseEntity<Void> unconfirmCheckPost(@PathVariable int checkPostId){
        checkPostService.unconfirmCheckPost(checkPostId);
        return ResponseEntity.noContent().build();
    }
}
