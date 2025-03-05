package com.openclassrooms.springsecurityauth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalDTO {
    private Long id;
    private String name;
    private double surface;
    private double price;
    private String picture;
    private String description;
    private Long ownerId;
}
