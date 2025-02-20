package com.achiveme.mvp.dto.checkPost;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

//TODO sprawdzić jak updatetować tylko to co się zmieniło.
public record CheckPostUpdateRequestDTO(
        @NotNull(message = "Title cannot be null")
        @NotBlank(message = "Title cannot be blank")
        @Size(min = 2, max = 50, message = "Title must be between 2 and 50 characters")
        String title,

        @NotNull(message = "Title cannot be null")
        @NotBlank(message = "Title cannot be blank")
        String description

) {

}
