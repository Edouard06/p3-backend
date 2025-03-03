package com.openclassrooms.springsecurityauth.mapper;

import com.openclassrooms.springsecurityauth.dto.UserDTO;
import com.openclassrooms.springsecurityauth.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO userToUserDTO(User user);
    User userDTOToUser(UserDTO userDTO);
}
