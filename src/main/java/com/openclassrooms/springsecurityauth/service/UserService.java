package com.openclassrooms.springsecurityauth.service; // 1. Déclaration de package

import com.openclassrooms.springsecurityauth.model.User; // 2. Import de la classe User
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService { 

    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        return new User(user.getEmail(), encryptedPassword, user.getname());
    }
}
