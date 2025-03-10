package com.openclassrooms.springsecurityauth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginDTO {

    @NotBlank(message = "L'email ne doit pas être vide")
    @Email(message = "L'email doit être valide")
    private String email;

    @NotBlank(message = "Le mot de passe ne doit pas être vide")
    private String password;

    public LoginDTO() {
    }

    public LoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters et setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
