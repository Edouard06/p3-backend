package com.openclassrooms.springsecurityauth.controller;

import com.openclassrooms.springsecurityauth.dto.RentalDTO;
import java.util.List;

/**
 * Wrapper class for sending a list of rentals as a structured response.
 */
public class RentalResponse {

    private List<RentalDTO> rentals;

    public RentalResponse() {
        // Default constructor
    }

    public RentalResponse(List<RentalDTO> rentals) {
        this.rentals = rentals;
    }

    public List<RentalDTO> getRentals() {
        return rentals;
    }

    public void setRentals(List<RentalDTO> rentals) {
        this.rentals = rentals;
    }
}
