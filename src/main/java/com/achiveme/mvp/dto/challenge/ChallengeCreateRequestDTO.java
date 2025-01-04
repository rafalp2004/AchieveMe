package com.achiveme.mvp.dto.challenge;

import com.achiveme.mvp.enums.ChallengePurpose;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ChallengeCreateRequestDTO(

        @NotBlank(message = "Title cannot be blank.")
        @Size(max = 255, message = "Title cannot exceed 255 characters.")
        String title,

        @NotNull(message = "Purpose cannot be null.")
        ChallengePurpose purpose,

        @NotBlank(message = "Description cannot be blank.")
        @Size(max = 1000, message = "Description cannot exceed 1000 characters.")
        String description,

        @NotNull(message = "Deadline cannot be null.")
        @Future(message = "Deadline must be in the future.")
        LocalDateTime deadline,

        @NotNull(message = "Start date cannot be null.")
        @Future(message = "Start date must be in the future.")
        LocalDateTime startAt,

        @NotNull(message = "Entry fee cannot be null.")
        @DecimalMin(value = "30.00", inclusive = true, message = "Entry fee cannot be under 30.")
        BigDecimal entryFee,

        @NotNull(message = "Visibility flag must be specified.")
        boolean isPublic
) {
}
