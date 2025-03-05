package com.openclassrooms.springsecurityauth.dto;

public class RentalDTO {

    private Long id;
    private String name;
    private String description;
    private float price;
    private float surface;
    private String picture;

    // Constructeur sans arguments
    public RentalDTO() {
    }

    // Constructeur avec tous les arguments
    public RentalDTO(Long id, String name, String description, float price, float surface, String picture) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.surface = surface;
        this.picture = picture;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getSurface() {
        return surface;
    }

    public void setSurface(float surface) {
        this.surface = surface;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
