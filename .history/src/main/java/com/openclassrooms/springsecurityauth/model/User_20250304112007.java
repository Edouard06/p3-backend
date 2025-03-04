package com.openclassrooms.springsecurityauth.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String username;

    private User(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public static User of(String email, String password, String username) {
        return new User(email, password, username);
    }
}
