package com.achiveme.mvp.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserChangePasswordRequestDTO(
        @NotBlank(message = "Password cannot be blank.")
        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
        String password
) {
}
