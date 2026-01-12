package com.example.sessionbasedauthentication.controller;

import com.example.sessionbasedauthentication.dto.ChangePasswordDTO;
import com.example.sessionbasedauthentication.dto.CreateUserRequestDTO;
import com.example.sessionbasedauthentication.dto.UpdateUserRequestDTO;
import com.example.sessionbasedauthentication.dto.UserResponseDTO;
import com.example.sessionbasedauthentication.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")  // POST /admin/users/create
    public String createUser(
            @Valid @ModelAttribute CreateUserRequestDTO dto,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "admin/create-user";
        }

        userService.createUser(dto);
        return "redirect:/admin/users";
    }


    @PostMapping("/{id}/update")  // POST /admin/users/{id}/update
    public String updateUser(@Valid @ModelAttribute UpdateUserRequestDTO dto,
                             @PathVariable Long id) {
        userService.updateUser(id, dto);
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/delete")  // POST /admin/users/{id}/delete
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

    @GetMapping  // GET /admin/users
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }
    @GetMapping("/{id}")  // GET /admin/users/{id}
    public String getUserById(@PathVariable Long id, Model model) {
        UserResponseDTO user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "admin/user-details";
    }

    @PostMapping("/{id}/enable")  // POST /admin/users/{id}/enable
    public String enableUser(@PathVariable Long id) {
        userService.enableUser(id);
        return "redirect:/admin/users";
    }
    // Display the create user form
    @GetMapping("/create")
    public String showCreateUserForm(Model model) {
        model.addAttribute("createUserDTO", new CreateUserRequestDTO("", "", "", "", "", ""));
        return "admin/create-user";
    }

    // Display the update user form
    @GetMapping("/{id}/update")
    public String showUpdateUserForm(@PathVariable Long id, Model model) {
        UserResponseDTO user = userService.getUserById(id);

        // Create UpdateUserRequestDTO from the existing user data
        UpdateUserRequestDTO updateDTO = new UpdateUserRequestDTO(
                user.firstName(),
                user.lastName(),
                user.email(),
                user.roles().iterator().next(),
                true
        );

        model.addAttribute("updateUserDTO", updateDTO);
        model.addAttribute("userId", id);
        return "admin/update-user";
    }


}