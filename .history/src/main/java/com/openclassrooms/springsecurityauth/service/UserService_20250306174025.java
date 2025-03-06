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

    // Récupère tous les utilisateurs
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Supprime un utilisateur par son identifiant
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Sauvegarde (ajoute ou met à jour) un utilisateur
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Récupère un utilisateur par son identifiant
    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    // Met à jour un utilisateur existant
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
            // Supposons que UserRepository possède une méthode findByUsername
            User user = userRepository.findByUsername(username);
            if (user == null) {
                throw new UserNotFoundException("There is no user currently authenticated");
            }
            return user;
        }
        throw new UserNotFoundException("There is no user currently authenticated");
    }

    // Recherche un utilisateur par ID (retourne Optional)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
