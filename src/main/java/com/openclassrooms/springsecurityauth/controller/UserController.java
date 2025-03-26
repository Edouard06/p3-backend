package com.openclassrooms.springsecurityauth.controller;

import com.openclassrooms.springsecurityauth.dto.UserDTO;
import com.openclassrooms.springsecurityauth.mapper.UserMapper;
import com.openclassrooms.springsecurityauth.model.User;
import com.openclassrooms.springsecurityauth.service.CustomUserDetails;
import com.openclassrooms.springsecurityauth.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    /**
     * Récupère tous les utilisateurs.
     *
     * @return Liste des utilisateurs
     */
    // @GetMapping
    // public ResponseEntity<List<UserDTO>> getAllUsers() {
    //     List<User> users = userService.getAllUsers();
    //     List<UserDTO> dtos = users.stream()
    //             .map(userMapper::userToUserDTO)
    //             .collect(Collectors.toList());
    //     return ResponseEntity.ok(dtos);
    // }
    @GetMapping
public ResponseEntity<UserDTO> getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

    User user = userService.getUserById(userDetails.getId());
    if (user == null) {
        return ResponseEntity.notFound().build();  // Si l'utilisateur n'existe pas
    }

    return ResponseEntity.ok(userMapper.userToUserDTO(user));
}


    /**
     * Récupère un utilisateur par son identifiant. Seul l'utilisateur authentifié peut accéder à ses informations.
     *
     * @param id Identifiant de l'utilisateur
     * @return Détails de l'utilisateur
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        // Vérifier que l'utilisateur est authentifié et que l'utilisateur qui fait la requête est bien autorisé
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        
        // Vérification que l'utilisateur authentifié est bien celui qui fait la demande ou est un administrateur
        if (userDetails != null && userDetails.getId().equals(id)) {
            User user = userService.getUserById(id);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(userMapper.userToUserDTO(user));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();  // Retour 403 si l'utilisateur tente d'accéder à un autre utilisateur
        }
    }

    /**
     * Met à jour un utilisateur. Seul l'utilisateur authentifié peut mettre à jour ses propres informations.
     *
     * @param id L'identifiant de l'utilisateur
     * @param userDTO Les nouvelles informations de l'utilisateur
     * @return L'utilisateur mis à jour
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        // Récupérer l'utilisateur authentifié
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // Vérifier si l'utilisateur authentifié est bien celui qui tente de mettre à jour ses propres informations
        if (!userDetails.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // L'utilisateur n'est pas autorisé
        }

        // Si l'utilisateur est authentifié et est le bon, on procède à la mise à jour
        User user = userMapper.userDTOToUser(userDTO);
        User updatedUser = userService.updateUser(id, user);
        
        if (updatedUser == null) {
            return ResponseEntity.notFound().build(); // Si l'utilisateur n'a pas été trouvé
        }

        return ResponseEntity.ok(userMapper.userToUserDTO(updatedUser));
    }

    /**
     * Supprime un utilisateur. Seul un administrateur ou l'utilisateur lui-même peut supprimer son compte.
     *
     * @param id L'identifiant de l'utilisateur
     * @return Confirmation de suppression
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // Vérifier si l'utilisateur authentifié est bien celui qui tente de supprimer son compte
        if (userDetails.getId().equals(id)) {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();  // Retour 403 si l'utilisateur tente de supprimer un autre compte
        }
    }
}
