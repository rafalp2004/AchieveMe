package com.achiveme.mvp.service;


import com.achiveme.mvp.dto.user.UserCreateRequestDTO;
import com.achiveme.mvp.dto.user.UserResponseDTO;
import com.achiveme.mvp.entity.Role;
import com.achiveme.mvp.entity.User;
import com.achiveme.mvp.mapper.UserMapper;
import com.achiveme.mvp.repository.RoleRepository;
import com.achiveme.mvp.repository.UserRepository;
import com.achiveme.mvp.service.user.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);


    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void createUser_withValidData_returnsUserResponseDTO() {
        // Arrange
        UserCreateRequestDTO userCreateRequestDTO = new UserCreateRequestDTO(
                "John",
                "Snow",
                "john@snow.pl",
                "password"
        );
        UserResponseDTO userResponseDTO = new UserResponseDTO(
                1,
                "Snow",
                "john@snow.pl",
                LocalDate.now()
        );

        Role role = new Role();
        role.setName("ROLE_USER");

        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));
        when(userMapper.userToUserDTO(any(User.class))).thenReturn(userResponseDTO);

        // Act
        UserResponseDTO userResponse = userService.createUser(userCreateRequestDTO);

        // Assert
        assertEquals(userResponseDTO, userResponse);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(roleRepository).findByName("ROLE_USER");
        verify(userRepository).save(userCaptor.capture());
        verify(userMapper).userToUserDTO(any(User.class));

        User savedUser = userCaptor.getValue();
        assertEquals("John", savedUser.getFirstName());
        assertEquals("Snow", savedUser.getLastName());
        assertEquals("john@snow.pl", savedUser.getEmail());
        assertEquals("John_Snow", savedUser.getUsername());
        assertEquals(1, savedUser.getRoles().size());
        assertEquals("ROLE_USER", savedUser.getRoles().get(0).getName());
    }

}
