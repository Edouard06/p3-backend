package com.openclassrooms.springsecurityauth.mapper;

import com.openclassrooms.springsecurityauth.dto.RentalDTO;
import com.openclassrooms.springsecurityauth.dto.RentalCreateDTO;
import com.openclassrooms.springsecurityauth.model.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RentalMapper {

    // Mapping Rental → RentalDTO
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "surface", target = "surface")
    @Mapping(source = "picture", target = "picture")
    @Mapping(source = "owner.id", target = "ownerId")
    RentalDTO rentalToRentalDTO(Rental rental);

    // Mapping RentalDTO → Rental
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "surface", target = "surface")
    @Mapping(source = "picture", target = "picture")
    @Mapping(target = "owner", ignore = true) // ✅ ajouté car owner est injecté manuellement dans le controller
    Rental rentalDTOToRental(RentalDTO dto);

    // ✅ Mapping RentalCreateDTO → Rental
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "surface", target = "surface")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "picture", ignore = true)
    @Mapping(target = "owner", ignore = true)
    Rental rentalCreateDTOToRental(RentalCreateDTO dto);
}
