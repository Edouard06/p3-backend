package com.openclassrooms.springsecurityauth.service;

import com.openclassrooms.springsecurityauth.model.User;
import com.openclassrooms.springsecurityauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        return userRepository.save(new User(user.getEmail(), encryptedPassword, user.getUsername()));
    }
    
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
