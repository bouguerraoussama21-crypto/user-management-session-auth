package com.example.sessionbasedauthentication.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordDTO(
        @NotBlank
        String oldPassword,
        @NotBlank
        String newPassword,
        @NotBlank
        String confirmPassword
) {}
