package com.openclassrooms.springsecurityauth.controller;

import com.openclassrooms.springsecurityauth.dto.RentalDTO;
import com.openclassrooms.springsecurityauth.mapper.RentalMapper;
import com.openclassrooms.springsecurityauth.model.Rental;
import com.openclassrooms.springsecurityauth.service.ImageService;
import com.openclassrooms.springsecurityauth.service.RentalService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/rentals")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication") // ← On précise ici que ce contrôleur requiert un JWT
public class RentalController {

    private final RentalService rentalService;
    private final RentalMapper rentalMapper;
    private final ImageService imageService;

    /**
     * Creates a new rental with an associated image.
     *
     * @param rentalDTO the rental data (excluding image URL)
     * @param image 
     * @return the created rental with the image URL set
     */
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RentalDTO> createRental(
            @RequestPart("rental") RentalDTO rentalDTO,
            @RequestPart("image") MultipartFile image) {

        String imageUrl = imageService.storeImage(image);
        rentalDTO.setPicture(imageUrl);

        Rental rental = rentalMapper.rentalDTOToRental(rentalDTO);
        Rental savedRental = rentalService.createRental(rental);
        RentalDTO savedDTO = rentalMapper.rentalToRentalDTO(savedRental);

        return new ResponseEntity<>(savedDTO, HttpStatus.CREATED);
    }
    


    @GetMapping
    public ResponseEntity<List<RentalDTO>> getAllRentals() {
        List<Rental> rentals = rentalService.getAllRentals();
        List<RentalDTO> dtos = rentals.stream()
                .map(rentalMapper::rentalToRentalDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalDTO> getRental(@PathVariable Long id) {
        return rentalService.getRental(id)
                .map(rental -> ResponseEntity.ok(rentalMapper.rentalToRentalDTO(rental)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
