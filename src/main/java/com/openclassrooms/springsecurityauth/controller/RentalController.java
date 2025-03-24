package com.openclassrooms.springsecurityauth.controller;

import com.openclassrooms.springsecurityauth.dto.RentalCreateDTO;
import com.openclassrooms.springsecurityauth.dto.RentalDTO;
import com.openclassrooms.springsecurityauth.mapper.RentalMapper;
import com.openclassrooms.springsecurityauth.model.Rental;
import com.openclassrooms.springsecurityauth.model.User;
import com.openclassrooms.springsecurityauth.repository.RentalRepository;
import com.openclassrooms.springsecurityauth.service.ImageService;
import com.openclassrooms.springsecurityauth.service.RentalService;
import com.openclassrooms.springsecurityauth.service.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rentals")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class RentalController {

    private final RentalService rentalService;
    private final RentalMapper rentalMapper;
    private final ImageService imageService;
    private final UserService userService;
    private final RentalRepository rentalRepository;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RentalDTO> createRental(
            @ModelAttribute RentalCreateDTO rentalCreateDTO,
            @RequestParam("picture") MultipartFile pictureFile) throws IOException {

        User currentUser = userService.getCurrentUser();

        String imageUrl = imageService.saveImage(pictureFile);

        Rental rental = rentalMapper.rentalCreateDTOToRental(rentalCreateDTO);
        rental.setPicture(imageUrl);
        rental.setOwner(currentUser);

        Rental savedRental = rentalRepository.save(rental);

        RentalDTO responseDto = rentalMapper.rentalToRentalDTO(savedRental);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<Map<String, List<RentalDTO>>> getAllRentals() {
        List<Rental> rentals = rentalService.getAllRentals();
        List<RentalDTO> dtos = rentals.stream()
                .map(rentalMapper::rentalToRentalDTO)
                .collect(Collectors.toList());

        Map<String, List<RentalDTO>> response = new HashMap<>();
        response.put("rentals", dtos); // clé attendue par le front
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalDTO> getRental(@PathVariable Long id) {
        return rentalService.getRental(id)
                .map(rental -> ResponseEntity.ok(rentalMapper.rentalToRentalDTO(rental)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/test-images")
    public List<String> testImages() {
        File folder = new File("/var/uploads/images/");
        return folder.exists() ? Arrays.asList(folder.list()) : List.of("Dossier introuvable");
    }

    @DeleteMapping("/{id}")
public ResponseEntity<Void> deleteRental(@PathVariable Long id) {
    rentalService.deleteRental(id);
    return ResponseEntity.noContent().build();
}

}
