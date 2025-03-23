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
@SecurityRequirement(name = "Bearer Authentication")
public class RentalController {

    private final RentalService rentalService;
    private final RentalMapper rentalMapper;
    private final ImageService imageService;

    /**
     * Creates a new rental.
     * The FormData must include text fields with keys: name, description, price, surface,
     * and a file with key "picture".
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RentalDTO> createRental(
            @ModelAttribute RentalDTO rentalDTO,
            @RequestPart("picture") MultipartFile picture) {

        if (picture != null && !picture.isEmpty()) {
            String imageUrl = imageService.storeImage(picture);
            rentalDTO.setPictureUrl(imageUrl);
        }

        Rental rental = rentalMapper.rentalDTOToRental(rentalDTO);
        Rental savedRental = rentalService.createRental(rental);
        RentalDTO savedDTO = rentalMapper.rentalToRentalDTO(savedRental);
        return new ResponseEntity<>(savedDTO, HttpStatus.CREATED);
    }

    /**
     * Gets all rentals.
     */
    @GetMapping
    public ResponseEntity<List<RentalDTO>> getAllRentals() {
        List<Rental> rentals = rentalService.getAllRentals();
        List<RentalDTO> dtos = rentals.stream()
                .map(rentalMapper::rentalToRentalDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Gets a rental by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RentalDTO> getRental(@PathVariable Long id) {
        return rentalService.getRental(id)
                .map(rental -> ResponseEntity.ok(rentalMapper.rentalToRentalDTO(rental)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
