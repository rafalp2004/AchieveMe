package com.achiveme.mvp.service.user;

import com.achiveme.mvp.dto.user.*;
import com.achiveme.mvp.entity.Role;
import com.achiveme.mvp.entity.User;
import com.achiveme.mvp.exception.Role.RoleDoesNotExistException;
import com.achiveme.mvp.exception.User.UnauthorizedException;
import com.achiveme.mvp.exception.User.UserDoesNotExistException;
import com.achiveme.mvp.mapper.UserMapper;
import com.achiveme.mvp.repository.RoleRepository;
import com.achiveme.mvp.repository.UserRepository;
import com.achiveme.mvp.service.JWT.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;

        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public UserResponseDTO createUser(UserCreateRequestDTO userDTO) {


        User createdUser = new User();
        Role role = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RoleDoesNotExistException("Role does not exist"));  //RoleUser

        createdUser.setFirstName(userDTO.firstName());
        createdUser.setLastName(userDTO.lastName());
        createdUser.setEmail(userDTO.email());
        createdUser.addRole(role);
        createdUser.setUsername(userDTO.firstName() + "_" + userDTO.lastName());
        createdUser.setPasswordHash(passwordEncoder.encode(userDTO.password()));
        createdUser.setJoinDate(LocalDate.now());
        userRepository.save(createdUser);

        log.info(createdUser.toString());
        return userMapper.userToUserDTO(createdUser);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::userToUserDTO).toList();
    }

    @Override
    public UserResponseDTO getUserById(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserDoesNotExistException("User with id: +" + id + " does not exist"));
        if (isCurrentUserAdmin() || getCurrentUser().getId() == user.getId()) {
            return userMapper.userToUserDTO(user);
        } else throw new UnauthorizedException("Don't have permission to this method");
    }

    @Override
    public void deleteUserById(int id) {
        if (isCurrentUserAdmin() || getCurrentUser().getId() == id) {
            User user = userRepository.findById(id).orElseThrow(() -> new UserDoesNotExistException("User with id: +" + id + " does not exist"));
            userRepository.delete(user);
        } else throw new UnauthorizedException("Don't have permission to this method");

    }

    @Override
    public UserResponseDTO updateUser(int id, UserUpdateRequestDTO userDTO) {
        if (isCurrentUserAdmin() || getCurrentUser().getId() == id) {
            User user = userRepository.findById(id).orElseThrow(() -> new UserDoesNotExistException("User with id: +" + id + " does not exist"));
            user.setUsername(userDTO.username());
            user.setFirstName(userDTO.firstName());
            user.setLastName(userDTO.lastName());
            user.setEmail(userDTO.email());
            userRepository.save(user);
            return userMapper.userToUserDTO(user);
        } else throw new UnauthorizedException("Don't have permission to this method");
    }

    @Override
    public void changePassword(int id, UserChangePasswordRequestDTO userDTO) {
        if (isCurrentUserAdmin() || getCurrentUser().getId() == id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserDoesNotExistException("User with id: +" + id + " does not exist"));
        user.setPasswordHash(passwordEncoder.encode(userDTO.password()));
        userRepository.save(user);
        } else throw new UnauthorizedException("Don't have permission to this method");
    }

    @Override
    public String verify(LoginUserDTO userDTO) {
        //TODO if invalid data is passed, method stops after next line.
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.email(), userDTO.password()));
        if (authentication.isAuthenticated()) {
            // return jwtService.generateToken(userDTO.email());
            return jwtService.generateToken(userDTO.email());
        }
        return "Fail";
    }

    @Transactional(readOnly = true)
    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("User is not authenticated");
        }

        String email = extractEmail(authentication);
        if (email == null || email.isEmpty()) {
            throw new UnauthorizedException("User email is not available");
        }

        return userRepository.findByEmail(email).orElseThrow(() -> new UserDoesNotExistException("User with email " + email + " does not exist"));
    }

    @Override
    public boolean isCurrentUserAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
    }

    private String extractEmail(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }

        return null;
    }
}
