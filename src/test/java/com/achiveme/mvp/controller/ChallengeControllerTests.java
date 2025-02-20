package com.achiveme.mvp.controller;


import com.achiveme.mvp.dto.challenge.ChallengeCreateRequestDTO;
import com.achiveme.mvp.dto.challenge.ChallengeResponseDTO;
import com.achiveme.mvp.dto.challenge.ChallengeUpdateRequestDTO;
import com.achiveme.mvp.enums.ChallengePurpose;
import com.achiveme.mvp.exception.Challenge.ChallengeDoesNotExistException;
import com.achiveme.mvp.service.JWT.JwtService;
import com.achiveme.mvp.service.challenge.ChallengeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = ChallengeController.class)
public class ChallengeControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChallengeService challengeService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @Test
    public void testGetChallengeById_Success() throws Exception {
        int challengeId = 1;
        ChallengeResponseDTO challengeResponseDTO = new ChallengeResponseDTO(
                challengeId,
                2,
                "title",
                "purpose",
                "description",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now(),
                BigDecimal.valueOf(5),
                true,
                List.of()
        );

        //Arrange
        Mockito.when(challengeService.getChallengeById(Mockito.eq(challengeId))).thenReturn(challengeResponseDTO);

        //Act & Assert

        mockMvc.perform(MockMvcRequestBuilders.get("/challenges/{id}", challengeId)
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(challengeId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("title"));

    }

    @Test
    public void testGetChallengeById_Failed() throws Exception {
        int challengeId = 999;

        //Arrange
        Mockito.when(challengeService.getChallengeById(Mockito.eq(challengeId)))
                .thenThrow(new ChallengeDoesNotExistException("Challenge Not Found"));

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/challenges/{id}", challengeId)
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testGetChallenges() throws Exception {
        List<ChallengeResponseDTO> challenges = List.of(
                new ChallengeResponseDTO(
                        1,
                        2,
                        "title",
                        "purpose",
                        "description",
                        LocalDateTime.now().plusDays(1),
                        LocalDateTime.now(),
                        BigDecimal.valueOf(5),
                        true,
                        List.of()
                ),
                new ChallengeResponseDTO(
                        2,
                        3,
                        "title2",
                        "purpose2",
                        "description2",
                        LocalDateTime.now().plusDays(1),
                        LocalDateTime.now(),
                        BigDecimal.valueOf(51),
                        false,
                        List.of()
                )
        );

        //Arrange
        Mockito.when(challengeService.getAllChallenges()).thenReturn(challenges);

        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/challenges")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2));


    }

    @Test
    public void testCreateChallenge_Success() throws Exception {

        ChallengeCreateRequestDTO challengeCreateRequestDTO = new ChallengeCreateRequestDTO(
                "tit",
                ChallengePurpose.LEARNING,
                "purpose",
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(1),
                BigDecimal.valueOf(50),
                true
        );
        ChallengeResponseDTO challengeResponseDTO = new ChallengeResponseDTO(
                1,
                2,
                "title",
                "learning",
                "description",
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(1),
                BigDecimal.valueOf(50),
                true,
                List.of()
        );

        // Arrange
        Mockito.when(challengeService.createChallenge(Mockito.any(ChallengeCreateRequestDTO.class)))
                .thenReturn(challengeResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/challenges")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(challengeCreateRequestDTO))
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    public void testCreateChallenge_Failed() throws Exception {

        ChallengeCreateRequestDTO challengeCreateRequestDTO = new ChallengeCreateRequestDTO(
                "tit",
                ChallengePurpose.LEARNING,
                "purpose",
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(1),
                BigDecimal.valueOf(5), // Bad input
                true
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/challenges")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(challengeCreateRequestDTO))
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void testUpdateChallenge_Success() throws Exception {
        int challengeId = 1;

        ChallengeUpdateRequestDTO challengeUpdateRequestDTO = new ChallengeUpdateRequestDTO(
                challengeId,
                "titleUpdated",
                "purpose",
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(1),
                BigDecimal.valueOf(50),
                true
        );
        ChallengeResponseDTO challengeResponseDTO = new ChallengeResponseDTO(
                challengeId,
                2,
                "titleUpdated",
                "learning",
                "description",
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(1),
                BigDecimal.valueOf(50),
                true,
                List.of()
        );

        // Arrange
        Mockito.when(challengeService.updateChallenge(Mockito.eq(challengeId),Mockito.any(ChallengeUpdateRequestDTO.class)))
                .thenReturn(challengeResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.patch("/challenges/{id}", challengeId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(challengeUpdateRequestDTO))
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("titleUpdated"));

    }

    @Test
    public void testUpdateChallenge_Failed() throws Exception {
        int challengeId = 1;

        ChallengeUpdateRequestDTO challengeUpdateRequestDTO = new ChallengeUpdateRequestDTO(
                challengeId,
                "titleUpdated",
                "purpose",
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(1),
                BigDecimal.valueOf(50),
                true
        );

        // Arrange
        Mockito.when(challengeService.updateChallenge(Mockito.eq(challengeId),Mockito.any(ChallengeUpdateRequestDTO.class)))
                .thenThrow(new ChallengeDoesNotExistException("Challenge not found"));


        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.patch("/challenges/{id}", challengeId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(challengeUpdateRequestDTO))
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());


    }

    @Test
    public void testDeleteChallengeById_Success() throws Exception {
        int challengeId = 1;
        Mockito.doNothing().when(challengeService).deleteChallengeById(Mockito.eq(challengeId));

        mockMvc.perform(MockMvcRequestBuilders.delete("/challenges/{id}", challengeId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testDeleteChallengeById_Failed() throws Exception {
        int challengeId = 99999;
        Mockito.doThrow(new ChallengeDoesNotExistException("Challenge not found")).when(challengeService).deleteChallengeById(Mockito.eq(challengeId));

        mockMvc.perform(MockMvcRequestBuilders.delete("/challenges/{id}", challengeId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
