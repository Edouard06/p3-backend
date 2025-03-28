package com.openclassrooms.springsecurityauth.mapper;

import com.openclassrooms.springsecurityauth.dto.MessageDTO;
import com.openclassrooms.springsecurityauth.model.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(source = "content", target = "message")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "rentalId", target = "rentalId")
    MessageDTO toDto(Message message);

    @Mapping(source = "message", target = "content")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "rentalId", target = "rentalId")
    Message toEntity(MessageDTO dto);
}
