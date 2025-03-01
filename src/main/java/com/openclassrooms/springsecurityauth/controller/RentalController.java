package com.openclassrooms.springsecurityauth.controller;

import com.openclassrooms.springsecurityauth.model.Rental;
import com.openclassrooms.springsecurityauth.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    // GET /rentals
    @GetMapping
    public List<Rental> getAll() {
        return rentalService.getAllRentals();
    }

    // GET /rentals/{id}
    @GetMapping("/{id}")
    public Rental get(@PathVariable Long id) {
        return rentalService.getRental(id).orElse(null);
    }

    // POST /rentals/create
    @PostMapping("/create")
    public Rental create(@RequestBody Rental rental) {
        return rentalService.createRental(rental);
    }

    // PUT /rentals/{id}
    @PutMapping("/{id}")
    public Rental update(@PathVariable Long id, @RequestBody Rental rentalData) {
        return rentalService.updateRental(id, rentalData);
    }
}
