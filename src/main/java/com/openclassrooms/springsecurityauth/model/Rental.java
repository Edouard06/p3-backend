package com.openclassrooms.springsecurityauth.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;       
    private double surface;
    private double price;
    private String picture;    
    private String description;

    // Relation avec un propriétaire
    private Long ownerId;      
    // Vous pourriez aussi faire un ManyToOne vers User si vous voulez une relation JPA

    // Constructeurs
    public Rental() {}

    public Rental(String name, double surface, double price, String picture, String description, Long ownerId) {
        this.name = name;
        this.surface = surface;
        this.price = price;
        this.picture = picture;
        this.description = description;
        this.ownerId = ownerId;
    }

    // Getters / Setters
    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getSurface() { return surface; }
    public void setSurface(double surface) { this.surface = surface; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getPicture() { return picture; }
    public void setPicture(String picture) { this.picture = picture; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
}
