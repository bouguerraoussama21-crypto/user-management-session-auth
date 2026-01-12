package com.example.sessionbasedauthentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequestDTO(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @Email
        String email
) {
}
