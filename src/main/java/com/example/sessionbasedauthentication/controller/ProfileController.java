package com.example.sessionbasedauthentication.controller;

import com.example.sessionbasedauthentication.dto.ChangePasswordDTO;
import com.example.sessionbasedauthentication.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasRole('USER')")
public class ProfileController {

        private final UserService userService;

        public ProfileController(UserService userService) {
            this.userService = userService;
        }

        @GetMapping("/change-password")
        public String showForm(Model model){
            model.addAttribute("passwordDTO", new ChangePasswordDTO("","",""));
            return "change-password";
        }

        @PostMapping("/change-password")
        public String changePassword(
                @Valid @ModelAttribute ChangePasswordDTO dto,
                BindingResult result,
                Principal principal
        ){
            if(result.hasErrors()){
                return "change-password";
            }

            userService.changePassword(principal.getName(), dto);
            return "redirect:/dashboard";
        }


}
