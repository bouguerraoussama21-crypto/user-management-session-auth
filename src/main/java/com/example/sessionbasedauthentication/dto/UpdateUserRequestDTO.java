package com.example.sessionbasedauthentication.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequestDTO(
        @NotBlank
        String firstName ,
        @NotBlank
        String lastName ,
        @NotBlank
        String email ,
        @NotBlank
        String roleName  ,
        boolean enabled
) {
}
