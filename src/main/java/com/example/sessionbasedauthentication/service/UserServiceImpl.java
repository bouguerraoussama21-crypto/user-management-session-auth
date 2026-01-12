package com.example.sessionbasedauthentication.service;

import com.example.sessionbasedauthentication.dto.ChangePasswordDTO;
import com.example.sessionbasedauthentication.dto.CreateUserRequestDTO;
import com.example.sessionbasedauthentication.dto.UpdateUserRequestDTO;
import com.example.sessionbasedauthentication.dto.UserResponseDTO;
import com.example.sessionbasedauthentication.model.Role;
import com.example.sessionbasedauthentication.model.User;
import com.example.sessionbasedauthentication.repository.RoleRepository;
import com.example.sessionbasedauthentication.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;
    public UserServiceImpl(UserRepository userRepository,RoleRepository roleRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository=roleRepository;
        this.passwordEncoder=passwordEncoder;
    }
    private UserResponseDTO convertToDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()),
                user.getCreatedAt()
        );
    }
    @Override
    public UserResponseDTO createUser(CreateUserRequestDTO createUserRequestDTO) {
        Role role = roleRepository.findByName(createUserRequestDTO.roleName())
                .orElseThrow(() -> new RuntimeException("Role not found"));
        if(userRepository.findByUserName(createUserRequestDTO.username()).isPresent())
            throw new IllegalArgumentException("Username already exists");
        if(userRepository.findByEmail(createUserRequestDTO.email()).isPresent())
            throw new IllegalArgumentException("Email already exists");
        User user = new User();
        user.setUserName(createUserRequestDTO.username());
        user.setEmail(createUserRequestDTO.email());
        user.setPassword(passwordEncoder.encode(createUserRequestDTO.password()));
        user.setFirstName(createUserRequestDTO.firstName());
        user.setLastName(createUserRequestDTO.lastName());
        user.setRoles(Set.of(role));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    @Override
    public UserResponseDTO updateUser(Long id, UpdateUserRequestDTO updateUserRequestDTO) {
        User existUser=userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found"));
        User userWithSameEmail = userRepository
                .findByEmail(updateUserRequestDTO.email())
                .orElse(null);

        if(userWithSameEmail != null && !userWithSameEmail.getId().equals(id)) {
            throw new IllegalArgumentException("Email already exists");
        }
        Role role = roleRepository
                .findByName(updateUserRequestDTO.roleName())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        existUser.setFirstName(updateUserRequestDTO.firstName());
        existUser.setLastName(updateUserRequestDTO.lastName());
        existUser.setEmail(updateUserRequestDTO.email());
        existUser.setRoles(new HashSet<>(Set.of(role)));
        existUser.setEnabled(updateUserRequestDTO.enabled());
        User savedUser=userRepository.save(existUser);
        return convertToDTO(savedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User existUser= userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found"));
        userRepository.delete(existUser);
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User existUser= userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found"));
        return convertToDTO(existUser);

    }

    @Override
    public List<UserResponseDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO enableUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEnabled(true);
        User saved = userRepository.save(user);

        return  convertToDTO(saved);
    }
@Override
    public void changePassword(String username, ChangePasswordDTO changePasswordDTO){

        User user = userRepository.findByUserName(username)
                .orElseThrow();

        if(!passwordEncoder.matches(changePasswordDTO.oldPassword(), user.getPassword())){
            throw new RuntimeException("Wrong old password");
        }

        if(!changePasswordDTO.newPassword().equals(changePasswordDTO.confirmPassword())){
            throw new RuntimeException("Passwords not match");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDTO.newPassword()));
        userRepository.save(user);
    }

}
