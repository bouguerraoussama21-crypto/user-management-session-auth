package com.example.sessionbasedauthentication.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public record UserResponseDTO(
        Long id,
        String userName,
        String email,
        String firstName,
        String lastName,
        Set<String> roles,
        LocalDateTime createdAt
) {
}
