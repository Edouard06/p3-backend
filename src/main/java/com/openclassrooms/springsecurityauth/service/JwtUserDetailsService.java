package com.openclassrooms.springsecurityauth.service;

import com.openclassrooms.springsecurityauth.model.User;
import com.openclassrooms.springsecurityauth.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
        return new CustomUserDetails(user);
    }

    public CustomUserDetails loadUserByUserEmail(String email) throws UsernameNotFoundException {
        return (CustomUserDetails) loadUserByUsername(email);
    }
}
