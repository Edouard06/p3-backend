package com.openclassrooms.springsecurityauth.service;

import com.openclassrooms.springsecurityauth.model.Rental;
import com.openclassrooms.springsecurityauth.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    public Rental createRental(Rental rental) {
        return rentalRepository.save(rental);
    }

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public Optional<Rental> getRental(Long id) {
        return rentalRepository.findById(id);
    }

    public Rental updateRental(Long id, Rental rentalData) {
        Optional<Rental> optionalRental = rentalRepository.findById(id);
        if (optionalRental.isPresent()) {
            Rental existingRental = optionalRental.get();
            existingRental.setName(rentalData.getName());
            existingRental.setSurface(rentalData.getSurface());
            existingRental.setPrice(rentalData.getPrice());
            existingRental.setPicture(rentalData.getPicture());
            existingRental.setDescription(rentalData.getDescription());
            
            return rentalRepository.save(existingRental);
        }
        return null; 
    }
}
