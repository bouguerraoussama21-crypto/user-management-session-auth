package com.example.sessionbasedauthentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateUserRequestDTO(
        @NotBlank
        String username ,
        @Email
        @NotBlank
        String email ,
        @NotBlank
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "The password must contain at least 8 characters, including an uppercase letter, a lowercase letter, a number, and a special character"
        )
        String password ,
        @NotBlank
        String firstName ,
        @NotBlank
        String lastName ,
        @NotBlank
        String roleName
        ) {
}
