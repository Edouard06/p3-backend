package com.openclassrooms.springsecurityauth.controller;

import com.openclassrooms.springsecurityauth.dto.UserDTO;
import com.openclassrooms.springsecurityauth.mapper.UserMapper;
import com.openclassrooms.springsecurityauth.model.User;
import com.openclassrooms.springsecurityauth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(summary = "Inscription d'un nouvel utilisateur", responses = {
            @ApiResponse(responseCode = "201", description = "Utilisateur créé avec succès")
    })
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDTO) {
        User user = userMapper.userDTOToUser(userDTO);
        User createdUser = userService.saveUser(user);
        UserDTO createdDTO = userMapper.userToUserDTO(createdUser);
        return new ResponseEntity<>(createdDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Connexion d'un utilisateur", responses = {
            @ApiResponse(responseCode = "200", description = "Connexion réussie")
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO) {
        String token = "JWT_TOKEN";
        return ResponseEntity.ok(token);
    }

    @Operation(summary = "Récupère les informations de l'utilisateur connecté", responses = {
            @ApiResponse(responseCode = "200", description = "Récupération réussie de l'utilisateur")
    })
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        User user = User.of("test@test.com", "secret", "TestUser");
        UserDTO userDTO = userMapper.userToUserDTO(user);
        return ResponseEntity.ok(userDTO);
    }
}
