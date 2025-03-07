package com.openclassrooms.springsecurityauth.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.openclassrooms.springsecurityauth.exceptions.UserNotFoundException;
import com.openclassrooms.springsecurityauth.model.User;
import com.openclassrooms.springsecurityauth.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    public User updateUser(Long id, User user) {
        User existingUser = getUserById(id);
        if (existingUser == null) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        existingUser.setEmail(user.getEmail());
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());
        return userRepository.save(existingUser);
    }

    // Récupère l'utilisateur actuellement authentifié
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            User user = userRepository.findByUsername(username);
            if (user == null) {
                throw new UserNotFoundException("There is no user currently authenticated");
            }
            return user;
        }
        throw new UserNotFoundException("There is no user currently authenticated");
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
