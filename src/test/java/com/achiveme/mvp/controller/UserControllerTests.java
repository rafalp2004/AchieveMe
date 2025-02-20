package com.achiveme.mvp.controller;

import com.achiveme.mvp.dto.user.UserChangePasswordRequestDTO;
import com.achiveme.mvp.dto.user.UserCreateRequestDTO;
import com.achiveme.mvp.dto.user.UserResponseDTO;
import com.achiveme.mvp.dto.user.UserUpdateRequestDTO;
import com.achiveme.mvp.exception.User.UserDoesNotExistException;
import com.achiveme.mvp.service.JWT.JwtService;
import com.achiveme.mvp.service.user.UserService;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;


@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @Test
    public void testCreateUser_Success() throws Exception {
        UserCreateRequestDTO userCreateRequestDTO = new UserCreateRequestDTO("John", "Snow", "john@gmail.com", "password");
        UserResponseDTO userResponseDTO = new UserResponseDTO(1, "John", "Snow", LocalDate.now());

        Mockito.when(userService.createUser(Mockito.any(UserCreateRequestDTO.class)))
                .thenReturn(userResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateRequestDTO))
                ).
                andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/users/1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Snow"));
    }

    @Test
    public void testCreateUser_Failed() throws Exception {
        UserCreateRequestDTO invalidRequest = new UserCreateRequestDTO("", "Snow", "john@gmail.com", "password");

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testGetUserById_Success() throws Exception {
        int userId = 1;
        UserResponseDTO userResponseDTO = new UserResponseDTO(userId, "John", "Snow", LocalDate.now());

        // Arrange
        Mockito.when(userService.getUserById(userId)).thenReturn(userResponseDTO);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(userId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Snow"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdDate").exists());
    }


    @Test
    public void testGetUserById_Failed() throws Exception {
        int userId = 99999;

        // Arrange
        Mockito.when(userService.getUserById(userId))
                .thenThrow(new UserDoesNotExistException("User not found"));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", userId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    public void testDeleteUserById_Success() throws Exception {
        int userId = 1;
        //Arrange
        Mockito.doNothing().when(userService).deleteUserById(userId);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", userId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(userService, Mockito.times(1)).deleteUserById(userId);

    }

    @Test
    public void testGetAllUsers_Success() throws Exception {
        List<UserResponseDTO> users = List.of(
                new UserResponseDTO(1, "John", "Snow", LocalDate.of(2025, 3, 15)),
                new UserResponseDTO(2, "Marry", "Nowak", LocalDate.of(2025, 3, 15))
        );
        // Arrange
        Mockito.when(userService.getAllUsers()).thenReturn(users);

        // Act & Assert

        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value("Snow"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].createdDate").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].firstName").value("Marry"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].lastName").value("Nowak"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].createdDate").exists());
    }

    @Test
    public void testUpdateUser_Success() throws Exception {
        int userId = 1;

        // Arrange : Create a sample update request and a sample updated response
        UserUpdateRequestDTO userUpdateRequestDTO = new UserUpdateRequestDTO(userId, "JohnSnow69", "Jonathan", "Snow", "john@snow.pl");
        UserResponseDTO userResponseDTO = new UserResponseDTO(userId, "Jonathan", "Snow", LocalDate.of(2005, 2, 14));

        Mockito.when(userService.updateUser(Mockito.eq(userId), Mockito.any(UserUpdateRequestDTO.class)))
                .thenReturn(userResponseDTO);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.patch("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateRequestDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(userId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Jonathan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Snow"));
    }

    @Test
    public void testUpdateUser_Failed() throws Exception {
        int userId = 999;

        // Arrange : Create a sample update request and a sample updated response
        UserUpdateRequestDTO userUpdateRequestDTO = new UserUpdateRequestDTO(userId, "JohnSnow69", "Jonathan", "Snow", "john@snow.pl");
        UserResponseDTO userResponseDTO = new UserResponseDTO(userId, "Jonathan", "Snow", LocalDate.of(2005, 2, 14));


        Mockito.when(userService.updateUser(Mockito.eq(userId), Mockito.any(UserUpdateRequestDTO.class)))
                .thenThrow(new UserDoesNotExistException("User not found"));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.patch("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateRequestDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    public void testChangePassword_Success() throws Exception {
        int userId=1;
        UserChangePasswordRequestDTO userChangePasswordRequestDTO = new UserChangePasswordRequestDTO("newPassword");

        Mockito.doNothing().when(userService).changePassword(Mockito.eq(userId), Mockito.any(UserChangePasswordRequestDTO.class));

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/{id}/change-password", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userChangePasswordRequestDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }
    @Test
    public void testChangePassword_Failed() throws Exception {
        int userId=1;
        UserChangePasswordRequestDTO userChangePasswordRequestDTO = new UserChangePasswordRequestDTO("newPassword");

        Mockito.doThrow(new UserDoesNotExistException("User not found")).when(userService).changePassword(Mockito.eq(userId), Mockito.any(UserChangePasswordRequestDTO.class));

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/{id}/change-password", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userChangePasswordRequestDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

}
