package com.openclassrooms.springsecurityauth.mapper;

import com.openclassrooms.springsecurityauth.dto.RentalDTO;
import com.openclassrooms.springsecurityauth.model.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Mapper(componentModel = "spring")
public interface RentalMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "surface", target = "surface")
    @Mapping(source = "picture", target = "picture", qualifiedByName = "imageToBase64")
    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    RentalDTO rentalToRentalDTOWithBase64(Rental rental);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "surface", target = "surface")
    @Mapping(source = "picture", target = "picture")
    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    RentalDTO rentalToRentalDTO(Rental rental);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "surface", target = "surface")
    @Mapping(source = "picture", target = "picture")
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Rental rentalDTOToRental(RentalDTO dto);

    // SUPPRIME TOUTE CETTE METHODE avec annotations compris
    // Rental rentalCreateDTOToRental(RentalCreateDTO dto);

    @Named("imageToBase64")
    static String imageToBase64(String imageUrl) {
        if (imageUrl == null) return null;

        try {
            String filename = imageUrl.replace("/images/", "");
            Path imagePath = Paths.get("/home/edouard/uploads/images/" + filename);
            byte[] imageBytes = Files.readAllBytes(imagePath);
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
