package com.achiveme.mvp.controller;

import com.achiveme.mvp.dto.checkPost.CheckPostRequestDTO;
import com.achiveme.mvp.dto.checkPost.CheckPostResponseDTO;
import com.achiveme.mvp.dto.checkPost.CheckPostUpdateRequestDTO;
import com.achiveme.mvp.enums.CheckPostStatus;
import com.achiveme.mvp.service.JWT.JwtService;
import com.achiveme.mvp.service.challenge.ChallengeService;
import com.achiveme.mvp.service.checkPosts.CheckPostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = CheckPostController.class)
public class CheckPostControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CheckPostService checkPostService;

    @MockBean
    private ChallengeService challengeService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @Test
    public void testGetCheckPostById() throws Exception {
        int id = 1;

        CheckPostResponseDTO responseDTO = new CheckPostResponseDTO(
                id,
                10,
                "Test Title",
                "Test Description",
                CheckPostStatus.UNCONFIRMED,
                LocalDateTime.of(2023, 2, 20, 10, 0)
        );


        //Arrange
        when(checkPostService.getCheckPost(id)).thenReturn(responseDTO);


        //Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/challenges/check-posts/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.challengeId").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Test Title"));

    }

    @Test
    public void testGetAllCheckPosts() throws Exception {
        int challengeId = 10;

        List<CheckPostResponseDTO> responseDTOList = List.of(
                new CheckPostResponseDTO(
                        1,
                        challengeId,
                        "Test Title 1",
                        "Test Description 1",
                        CheckPostStatus.UNCONFIRMED,
                        LocalDateTime.of(2023, 2, 20, 10, 0)
                ),
                new CheckPostResponseDTO(
                        2,
                        challengeId,
                        "Test Title 2",
                        "Test Description 2",
                        CheckPostStatus.UNCONFIRMED,
                        LocalDateTime.of(2023, 2, 21, 11, 0)
                )
        );

        when(checkPostService.getAllChallengeCheckPosts(challengeId)).thenReturn(responseDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/challenges/{challengeId}/check-posts", challengeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].challengeId").value(challengeId))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Test Title 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].challengeId").value(challengeId))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Test Title 2"));
    }

    @Test
    public void testCreateCheckPost() throws Exception {
        int challengeId = 10;


        CheckPostRequestDTO checkPostRequestDTO = new CheckPostRequestDTO(
                "Test Title",
                "Test Description"
        );

        CheckPostResponseDTO checkPostResponseDTO = new CheckPostResponseDTO(
                1,                           // id
                challengeId,                 // challengeId
                "Test Title",                // title
                "Test Description",          // description
                CheckPostStatus.UNCONFIRMED, // checkPostStatus (przykładowy enum)
                LocalDateTime.of(2023, 2, 20, 10, 0) // publishedDate
        );

        when(checkPostService.createCheckPost(Mockito.eq(challengeId), any(CheckPostRequestDTO.class)))
                .thenReturn(checkPostResponseDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(checkPostRequestDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/challenges/{challengeId}/check-posts", challengeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(checkPostResponseDTO.id()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.challengeId").value(challengeId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(checkPostResponseDTO.title()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(checkPostResponseDTO.description()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.checkPostStatus").value(checkPostResponseDTO.checkPostStatus().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.publishedDate").value("2023-02-20T10:00:00"));
    }

    @Test
    public void testDeleteCheckPostById() throws Exception {
        int id = 1;

        mockMvc.perform(MockMvcRequestBuilders.delete("/challenges/check-posts/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(checkPostService, times(1)).deleteCheckPost(id);
    }

    @Test
    public void testUpdateCheckPost() throws Exception {
        int checkPostId = 1;
        CheckPostUpdateRequestDTO updateRequest = new CheckPostUpdateRequestDTO(
                "Updated Title",
                "Updated Description"
        );

        ObjectMapper objectMapper = new ObjectMapper();


        mockMvc.perform(MockMvcRequestBuilders.put("/challenges/check-posts/{checkPostId}", checkPostId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(checkPostService, times(1)).updateCheckPost(eq(checkPostId), any(CheckPostUpdateRequestDTO.class));
    }

    @Test
    public void testConfirmCheckPost() throws Exception {
        int checkPostId = 1;

        mockMvc.perform(MockMvcRequestBuilders.post("/challenges/check-posts/{checkPostId}/confirm", checkPostId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(checkPostService, times(1)).confirmCheckPost(checkPostId);
    }

    @Test
    public void testUnconfirmCheckPost() throws Exception {
        int checkPostId = 1;

        mockMvc.perform(MockMvcRequestBuilders.post("/challenges/check-posts/{checkPostId}/unconfirm", checkPostId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(checkPostService, times(1)).unconfirmCheckPost(checkPostId);
    }


}




