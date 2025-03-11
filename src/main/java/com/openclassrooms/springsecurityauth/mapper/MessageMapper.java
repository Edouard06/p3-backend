package com.openclassrooms.springsecurityauth.mapper;

import com.openclassrooms.springsecurityauth.dto.MessageDTO;
import com.openclassrooms.springsecurityauth.model.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(source = "content", target = "message")
    MessageDTO toDto(Message message);

    @Mapping(source = "message", target = "content")
    Message toEntity(MessageDTO dto);
}
