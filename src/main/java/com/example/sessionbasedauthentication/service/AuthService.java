package com.example.sessionbasedauthentication.service;

import com.example.sessionbasedauthentication.dto.RegisterRequestDTO;
import com.example.sessionbasedauthentication.dto.UserResponseDTO;

public interface AuthService {
    UserResponseDTO registerUser(RegisterRequestDTO registerRequestDTO);
}
