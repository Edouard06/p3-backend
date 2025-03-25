package com.openclassrooms.springsecurityauth.dto;

public class RentalCreateDTO {
    private String name;
    private String description;
    private Double price;
    private Double surface;

    public RentalCreateDTO() {}

    public RentalCreateDTO(String name, String description, Double price, Double surface) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.surface = surface;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Double getSurface() { return surface; }
    public void setSurface(Double surface) { this.surface = surface; }
}
