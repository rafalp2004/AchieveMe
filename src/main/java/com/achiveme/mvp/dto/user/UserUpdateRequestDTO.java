package com.achiveme.mvp.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserUpdateRequestDTO(

        @NotNull(message = "ID cannot be null")
        Integer id,

        @NotNull(message = "Username cannot be null")
        @NotBlank(message = "Username cannot be blank")
        @Size(min=2, max=50, message="Username must be between 2 and 50 characters")
        String username,


        @NotNull(message = "First name cannot be null")
        @NotBlank(message = "First name cannot be blank")
        @Size(min=2, max=50, message="First name must be between 2 and 50 characters")
        String firstName,

        @NotNull(message = "Last name cannot be null")
        @NotBlank(message = "Last name cannot be blank")
        @Size(min=2, max=50, message="Last name must be between 2 and 50 characters")
        String lastName,

        @NotNull(message = "Email cannot be null")
        @Email
        String email

        ) {
}
