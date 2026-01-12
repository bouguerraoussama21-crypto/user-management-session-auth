package com.example.sessionbasedauthentication.config;

import com.example.sessionbasedauthentication.model.Role;
import com.example.sessionbasedauthentication.model.User;
import com.example.sessionbasedauthentication.repository.RoleRepository;
import com.example.sessionbasedauthentication.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
        createDefaultAdmin();
    }

    private void initializeRoles() {
        // Create ROLE_USER if not exists
        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            userRole.setDescription("Default user role");
            roleRepository.save(userRole);
        }

        // Create ROLE_ADMIN if not exists
        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            adminRole.setDescription("Administrator role with full access");
            roleRepository.save(adminRole);
        }
    }

    private void createDefaultAdmin() {
        // Check if admin already exists
        if (userRepository.findByUserName("admin").isEmpty()) {
            // Get ROLE_ADMIN
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));

            // Create admin user
            User admin = new User();
            admin.setUserName("admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("oussama123"));
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setEnabled(true);
            admin.setAccountNonExpired(true);
            admin.setAccountNonLocked(true);
            admin.setCredentialsNonExpired(true);
            admin.setRoles(Set.of(adminRole));

            userRepository.save(admin);

        }
    }
}
