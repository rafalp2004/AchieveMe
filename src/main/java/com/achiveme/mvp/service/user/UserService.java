package com.achiveme.mvp.service.user;

import com.achiveme.mvp.dto.user.*;
import com.achiveme.mvp.entity.User;

import java.util.List;

public interface UserService {
    UserResponseDTO createUser(UserCreateRequestDTO userDTO);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(int id);

    void deleteUserById(int id);

    UserResponseDTO updateUser(int id, UserUpdateRequestDTO userDTO);

    void changePassword(int id, UserChangePasswordRequestDTO userDTO);

    String verify(LoginUserDTO userDTO);

    User getCurrentUser();

    boolean isCurrentUserAdmin();
}
