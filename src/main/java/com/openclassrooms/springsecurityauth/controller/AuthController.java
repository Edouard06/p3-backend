package com.openclassrooms.springsecurityauth.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.openclassrooms.springsecurityauth.dto.LoginDTO;
import com.openclassrooms.springsecurityauth.dto.RegisterDTO;
import com.openclassrooms.springsecurityauth.dto.UserDTO;
import com.openclassrooms.springsecurityauth.exceptions.UserAlreadyExistException;
import com.openclassrooms.springsecurityauth.service.CustomUserDetails;
import com.openclassrooms.springsecurityauth.service.UserService;
import com.openclassrooms.springsecurityauth.security.JwtTokenUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthController(JwtTokenUtil jwtTokenUtil,
                          AuthenticationManager authenticationManager,
                          UserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    // Handles user registration, auto-login, and JWT generation
    @PostMapping("/register")
    public ResponseEntity<JWTToken> register(@Valid @RequestBody RegisterDTO registerDto)
            throws UserAlreadyExistException {
        logger.info("Starting registration process for user: {}", registerDto.getEmail());

        userService.save(registerDto);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registerDto.getEmail(), registerDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenUtil.generateToken((CustomUserDetails) authentication.getPrincipal());
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION_HEADER, "Bearer " + jwt);

        logger.info("Registration and authentication successful for user: {}", registerDto.getEmail());
        return new ResponseEntity<>(new JWTToken(jwt), headers, HttpStatus.OK);
    }

    // Handles user login and JWT generation
    @PostMapping("/login")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginDTO loginDTO) {
        logger.info("Starting login process for user: {}", loginDTO.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenUtil.generateToken((CustomUserDetails) authentication.getPrincipal());
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION_HEADER, "Bearer " + jwt);

        logger.info("Login successful for user: {}", loginDTO.getEmail());
        return new ResponseEntity<>(new JWTToken(jwt), headers, HttpStatus.OK);
    }

    // Get currently authenticated user info
    @GetMapping("/me")
    public ResponseEntity<UserDTO> currentUser() {
        return ResponseEntity.ok(userService.getCurrentUserDTO());
    }

    // Handle duplicate user registration exception
    @ExceptionHandler({UserAlreadyExistException.class})
    public ResponseEntity<Map<String, String>> handleUserAlreadyExistException(UserAlreadyExistException ex) {
        logger.error("Registration error: {}", ex.getMessage());
        return new ResponseEntity<>(Map.of("error", ex.getMessage()), HttpStatus.CONFLICT);
    }

    // Handle validation errors
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.error("Validation error: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // Handle general server errors
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Map<String, String>> handleAllExceptions(Exception ex) {
        logger.error("Internal server error: {}", ex.getMessage());
        return new ResponseEntity<>(Map.of("error", "An internal server error occurred."), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Inner class for token response payload
    static class JWTToken {
        private String idToken;

        public JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("token")
        public String getIdToken() {
            return idToken;
        }

        public void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
