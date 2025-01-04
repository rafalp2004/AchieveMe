package com.achiveme.mvp.dto.user;

import java.time.LocalDate;

public record UserResponseDTO(
        int id,
        String firstName,
        String lastName,
        LocalDate createdDate
) {
}
