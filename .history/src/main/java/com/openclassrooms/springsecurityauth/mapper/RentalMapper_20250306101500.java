package com.openclassrooms.springsecurityauth.mapper;

import com.openclassrooms.springsecurityauth.dto.RentalDTO;
import com.openclassrooms.springsecurityauth.model.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RentalMapper {
    RentalDTO rentalToRentalDTO(Rental rental);
    Rental rentalDTOToRental(RentalDTO dto);
}
