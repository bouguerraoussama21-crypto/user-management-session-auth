package com.example.sessionbasedauthentication.service;

import com.example.sessionbasedauthentication.dto.ChangePasswordDTO;
import com.example.sessionbasedauthentication.dto.CreateUserRequestDTO;
import com.example.sessionbasedauthentication.dto.UpdateUserRequestDTO;
import com.example.sessionbasedauthentication.dto.UserResponseDTO;
import com.example.sessionbasedauthentication.model.Role;

import java.util.List;

public interface UserService {
    UserResponseDTO createUser(CreateUserRequestDTO createUserRequestDTO);
    UserResponseDTO updateUser(Long id, UpdateUserRequestDTO updateUserRequestDTO);
    void deleteUser(Long id);
    UserResponseDTO getUserById(Long id);
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO enableUser(Long id);
    void changePassword(String username, ChangePasswordDTO changePasswordDTO);
}
