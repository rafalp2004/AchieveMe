package com.achiveme.mvp.controller;

import com.achiveme.mvp.dto.challengeParticipant.ChallengeParticipantResponseDTO;
import com.achiveme.mvp.dto.challengeParticipant.ChallengeParticipantUpdateDTO;
import com.achiveme.mvp.enums.ParticipantStatus;
import com.achiveme.mvp.enums.PaymentStatus;
import com.achiveme.mvp.service.JWT.JwtService;
import com.achiveme.mvp.service.challengeParticipant.ChallengeParticipantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = ChallengeParticipantController.class)
public class ChallengeParticipantControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChallengeParticipantService challengeParticipantService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;


    @Test
    public void testCreateChallengeParticipant() throws Exception {
        int challengeID = 10;

        ChallengeParticipantResponseDTO createdParticipant = new ChallengeParticipantResponseDTO(1, challengeID, 2, LocalDateTime.now(), ParticipantStatus.ACTIVE, PaymentStatus.SUCCEEDED);

        when(challengeParticipantService.createParticipant(challengeID)).thenReturn(createdParticipant);

        mockMvc.perform(MockMvcRequestBuilders.post("/challenges/{challengeID}/participants", challengeID).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isCreated()).andExpect(MockMvcResultMatchers.header().string("Location", "/challenges/" + challengeID + "/participants/" + createdParticipant.id())).andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdParticipant.id())).andExpect(MockMvcResultMatchers.jsonPath("$.challengeId").value(challengeID));
    }

    @Test
    public void testGetChallengeParticipants() throws Exception {
        int challengeId = 10;

        List<ChallengeParticipantResponseDTO> participants = List.of(new ChallengeParticipantResponseDTO(1, challengeId, 2, LocalDateTime.now(), ParticipantStatus.ACTIVE, PaymentStatus.SUCCEEDED), new ChallengeParticipantResponseDTO(2, challengeId, 2, LocalDateTime.now(), ParticipantStatus.ACTIVE, PaymentStatus.SUCCEEDED));


        when(challengeParticipantService.getParticipantsByChallengeId(challengeId)).thenReturn(participants);


        mockMvc.perform(MockMvcRequestBuilders.get("/challenges/{challengeId}/participants", challengeId).contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1)).andExpect(MockMvcResultMatchers.jsonPath("$[0].challengeId").value(challengeId)).andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2)).andExpect(MockMvcResultMatchers.jsonPath("$[1].challengeId").value(challengeId));
    }

    @Test
    public void testDeleteChallengeParticipant() throws Exception {
        int challengeId = 10;
        int participantId = 1;

        mockMvc.perform(MockMvcRequestBuilders.delete("/challenges/{challengeId}/participants/{participantId}", challengeId, participantId)).andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(challengeParticipantService, times(1)).deleteParticipantById(challengeId, participantId);
    }


    @Test
    public void testUpdateStatusChallengeParticipant() throws Exception {
        int challengeId = 10;
        int participantId = 1;

        ChallengeParticipantUpdateDTO updateDTO = new ChallengeParticipantUpdateDTO(ParticipantStatus.ACTIVE, PaymentStatus.SUCCEEDED);

        ChallengeParticipantResponseDTO responseDTO = new ChallengeParticipantResponseDTO(1, challengeId, 2, LocalDateTime.now(), ParticipantStatus.ACTIVE, PaymentStatus.SUCCEEDED

        );


        when(challengeParticipantService.updateUserStatus(eq(challengeId), eq(participantId), any(ChallengeParticipantUpdateDTO.class))).thenReturn(responseDTO);


        mockMvc.perform(MockMvcRequestBuilders.patch("/challenges/{challengeId}/participants/{participantId}", challengeId, participantId).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateDTO))).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.id").value(responseDTO.id())).andExpect(MockMvcResultMatchers.jsonPath("$.challengeId").value(challengeId));
    }

}
