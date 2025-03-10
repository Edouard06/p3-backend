package com.openclassrooms.springsecurityauth.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.openclassrooms.springsecurityauth.dto.LoginDTO;
import com.openclassrooms.springsecurityauth.dto.RegisterDTO;
import com.openclassrooms.springsecurityauth.dto.UserDTO;
import com.openclassrooms.springsecurityauth.exceptions.UserAlreadyExistException;
import com.openclassrooms.springsecurityauth.service.UserService;
import com.openclassrooms.springsecurityauth.security.TokenProvider;
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
@CrossOrigin(origins = "*") // ou spécifie l'URL de ton front
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthController(TokenProvider tokenProvider,
                          AuthenticationManager authenticationManager,
                          UserService userService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    /**
     * 1) Inscription + génération de token
     */
    @PostMapping("/register")
    public ResponseEntity<JWTToken> register(@Valid @RequestBody RegisterDTO registerDto)
            throws UserAlreadyExistException {
        logger.info("Début de la méthode register pour l'inscription de l'utilisateur : {}", registerDto.getEmail());
        
        // 1) Création / sauvegarde du nouvel utilisateur
        userService.save(registerDto);

        // 2) Authentification immédiate après inscription
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(registerDto.getEmail(), registerDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3) Génération du token JWT
        String jwt = tokenProvider.createToken(authentication);

        // 4) Renvoi du token dans l'en-tête et dans le JSON
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION_HEADER, "Bearer " + jwt);
        logger.info("Inscription et authentification réussies pour l'utilisateur : {}", registerDto.getEmail());
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    /**
     * 2) Connexion (login) + génération de token
     */
    @PostMapping("/login")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginDTO loginDTO) {
        logger.info("Début de la méthode authorize pour la connexion de l'utilisateur : {}", loginDTO.getEmail());

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());
        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION_HEADER, "Bearer " + jwt);
        logger.info("Authentification réussie pour l'utilisateur : {}", loginDTO.getEmail());
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    /**
     * 3) Récupération de l’utilisateur courant (via token)
     */
    @GetMapping("/me")
    public ResponseEntity<UserDTO> currentUserName(Authentication authentication) {
        logger.info("Récupération de l'utilisateur courant pour : {}", authentication.getName());
        String email = authentication.getName();
        UserDTO userDto = userService.getUserByEmail(email);
        return ResponseEntity.ok(userDto);
    }

    // Gestion centralisée des exceptions spécifiques à ce contrôleur

    @ExceptionHandler({UserAlreadyExistException.class})
    public ResponseEntity<Map<String, String>> handleUserAlreadyExistException(UserAlreadyExistException ex) {
        logger.error("Erreur lors de l'inscription : {}", ex.getMessage());
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.error("Erreur de validation : {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Map<String, String>> handleAllExceptions(Exception ex) {
        logger.error("Erreur interne : {}", ex.getMessage());
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Une erreur interne s'est produite.");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Classe interne pour renvoyer le token dans la réponse JSON
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
