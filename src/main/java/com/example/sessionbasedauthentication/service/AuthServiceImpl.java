package com.example.sessionbasedauthentication.service;

import com.example.sessionbasedauthentication.dto.RegisterRequestDTO;
import com.example.sessionbasedauthentication.dto.UserResponseDTO;
import com.example.sessionbasedauthentication.model.Role;
import com.example.sessionbasedauthentication.model.User;
import com.example.sessionbasedauthentication.repository.RoleRepository;
import com.example.sessionbasedauthentication.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDTO registerUser(RegisterRequestDTO registerRequestDTO) {
        // Validate passwords match
        if (!registerRequestDTO.password().equals(registerRequestDTO.confirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // Check if username already exists
        if (userRepository.findByUserName(registerRequestDTO.userName()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Check if email already exists
        if (userRepository.findByEmail(registerRequestDTO.email()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setUserName(registerRequestDTO.userName());
        user.setEmail(registerRequestDTO.email());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.password()));
        user.setFirstName(registerRequestDTO.firstName());
        user.setLastName(registerRequestDTO.lastName());
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role USER not found"));
        user.setRoles(Set.of(userRole));
        User savedUser = userRepository.save(user);

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getUserName(),
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getRoles().stream().map(Role::getName).collect(Collectors.toSet()),
                savedUser.getCreatedAt()
        );
    }
}
