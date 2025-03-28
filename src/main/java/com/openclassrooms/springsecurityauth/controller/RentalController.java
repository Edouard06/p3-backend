package com.openclassrooms.springsecurityauth.controller;

import com.openclassrooms.springsecurityauth.dto.RentalDTO;
import com.openclassrooms.springsecurityauth.mapper.RentalMapper;
import com.openclassrooms.springsecurityauth.model.Rental;
import com.openclassrooms.springsecurityauth.model.User;
import com.openclassrooms.springsecurityauth.service.CustomUserDetails;
import com.openclassrooms.springsecurityauth.service.ImageService;
import com.openclassrooms.springsecurityauth.service.RentalService;
import com.openclassrooms.springsecurityauth.service.UserService;
import com.openclassrooms.springsecurityauth.repository.RentalRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rentals")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication") // Require JWT for access
public class RentalController {

    private final RentalService rentalService;
    private final RentalMapper rentalMapper;
    private final ImageService imageService;
    private final UserService userService;
    private final RentalRepository rentalRepository;

    /**
     * Create a new rental listing.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RentalDTO> createRental(
            @RequestPart("name") String name,
            @RequestPart("surface") Double surface,
            @RequestPart("price") Double price,
            @RequestPart("description") String description,
            @RequestPart("picture") MultipartFile pictureFile
    ) throws IOException {

        User currentUser = userService.getCurrentUser();
        String imageUrl = imageService.saveImage(pictureFile);

        Rental rental = new Rental();
        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);
        rental.setDescription(description);
        rental.setOwner(currentUser);
        rental.setPicture(imageUrl);

        Rental savedRental = rentalRepository.save(rental);
        RentalDTO responseDto = rentalMapper.rentalToRentalDTOWithBase64(savedRental);

        return ResponseEntity.ok(responseDto);
    }

    /**
     * Retrieve all rental listings.
     */
    @GetMapping
    public ResponseEntity<Map<String, List<RentalDTO>>> getAllRentals() {
        List<Rental> rentals = rentalService.getAllRentals();
        List<RentalDTO> dtos = rentals.stream()
                .map(rentalMapper::rentalToRentalDTOWithBase64)
                .collect(Collectors.toList());

        Map<String, List<RentalDTO>> response = new HashMap<>();
        response.put("rentals", dtos);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve a rental by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RentalDTO> getRental(@PathVariable Long id) {
        return rentalService.getRental(id)
                .map(rental -> ResponseEntity.ok(rentalMapper.rentalToRentalDTOWithBase64(rental)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Delete a rental if the authenticated user is the owner or admin.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRental(@PathVariable Long id, Authentication authentication) {
        Optional<Rental> rentalOpt = rentalService.getRental(id);
        if (rentalOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Rental rental = rentalOpt.get();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // Only allow owner or admin to delete the rental
        boolean isOwner = rental.getOwner().getId().equals(userDetails.getId());
        boolean isAdmin = userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        if (!isOwner && !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        rentalService.deleteRental(id);
        return ResponseEntity.noContent().build();
    }
}
