package com.achiveme.mvp.controller;

import com.achiveme.mvp.dto.user.UserChangePasswordRequestDTO;
import com.achiveme.mvp.dto.user.UserCreateRequestDTO;
import com.achiveme.mvp.dto.user.UserResponseDTO;
import com.achiveme.mvp.dto.user.UserUpdateRequestDTO;
import com.achiveme.mvp.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping(path="/users/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable int id){
        UserResponseDTO user = userService.getUserById(id);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @DeleteMapping(path="/users/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable int id){
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }



    @GetMapping(path="/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        List<UserResponseDTO> users = userService.getAllUsers();
        return new ResponseEntity<>(users,HttpStatus.OK);
    }


    //TODO error when there are 2 users with the same firstname and lastname
    @PostMapping(path="/users")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateRequestDTO userDTO) {
        UserResponseDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity
                .created(URI.create("/users/" + createdUser.id()))
                .body(createdUser);
    }

    //TODO Checking if all values are changing
    @PatchMapping(path="/users/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable int id,
            @Valid @RequestBody UserUpdateRequestDTO userDTO){
        UserResponseDTO updatedUser = userService.updateUser(id, userDTO);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PatchMapping(path="/users/{id}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable int id,
            @Valid@RequestBody UserChangePasswordRequestDTO userDTO) {
        userService.changePassword(id,userDTO);
        return ResponseEntity.noContent().build();
    }
}
