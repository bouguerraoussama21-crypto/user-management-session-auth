package com.example.sessionbasedauthentication.service;


import com.example.sessionbasedauthentication.config.UserDetailsImpl;
import com.example.sessionbasedauthentication.model.User;
import com.example.sessionbasedauthentication.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // find user by user name
        User user = userRepository.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return  new UserDetailsImpl(user);
    }
}
