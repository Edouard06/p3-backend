package com.openclassrooms.springsecurityauth.mapper;

import com.openclassrooms.springsecurityauth.dto.RentalDTO;
import com.openclassrooms.springsecurityauth.model.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RentalMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "surface", target = "surface")
    @Mapping(source = "pictureUrl", target = "picture") 
    Rental rentalDTOToRental(RentalDTO dto);

    @Mapping(source = "picture", target = "pictureUrl")
    RentalDTO rentalToRentalDTO(Rental rental);
}
