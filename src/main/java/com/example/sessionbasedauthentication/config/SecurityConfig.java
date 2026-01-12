package com.example.sessionbasedauthentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.
                authorizeHttpRequests(
                auth->auth
                        .requestMatchers("/","/home","/login","/register").permitAll() //Public page accessible to all types of users admin, user, or visitor
                        .requestMatchers("/admin/").hasRole("ADMIN")//Admin zone
                        .requestMatchers("/user/").hasRole("USER")//User zone
                        .anyRequest().authenticated() // Everything else requires authentication
        )
        .formLogin(form -> form
                .loginPage("/login")           // URL login page
                .defaultSuccessUrl("/dashboard", true)  // Redirect to dashboard after successful login
                .permitAll()
        )
                .logout(logout -> logout
                        .logoutUrl("/logout")          // URL to logout
                        .logoutSuccessUrl("/login?logout")  // Redirect after logout
                        .permitAll()
                );

        return httpSecurity.build();
    }
}
