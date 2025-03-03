package com.openclassrooms.springsecurityauth.controller;

import com.openclassrooms.springsecurityauth.dto.UserDTO;
import com.openclassrooms.springsecurityauth.mapper.UserMapper;
import com.openclassrooms.springsecurityauth.model.User;
import com.openclassrooms.springsecurityauth.service.UserService;
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

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDTO) {
        User user = userMapper.userDTOToUser(userDTO);
        User createdUser = userService.registerUser(user);
        UserDTO createdDTO = userMapper.userToUserDTO(createdUser);
        return new ResponseEntity<>(createdDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO) {
        String token = "JWT_TOKEN";
        return ResponseEntity.ok(token);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        User user = new User("test@test.com", "secret", "TestUser");
        UserDTO userDTO = userMapper.userToUserDTO(user);
        return ResponseEntity.ok(userDTO);
    }
}
