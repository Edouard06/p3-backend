package com.openclassrooms.springsecurityauth.dto;

public class RentalCreateDTO {
    private String name;
    private String description;
    private float price;
    private float surface;

    public RentalCreateDTO() {}

    public RentalCreateDTO(String name, String description, float price, float surface) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.surface = surface;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public float getPrice() { return price; }
    public void setPrice(float price) { this.price = price; }

    public float getSurface() { return surface; }
    public void setSurface(float surface) { this.surface = surface; }
}
