package com.openclassrooms.springsecurityauth.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long rentalId;  
    private Long userId;    // l'id user envoie message
    private String content; // contenu message

    public Message() {}

    public Message(Long rentalId, Long userId, String content) {
        this.rentalId = rentalId;
        this.userId = userId;
        this.content = content;
    }

    // Getters / Setters
    public Long getId() { return id; }

    public Long getRentalId() { return rentalId; }
    public void setRentalId(Long rentalId) { this.rentalId = rentalId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getMessage() { return content; }
    public void setMessage(String content) { this.content = content; }
}
