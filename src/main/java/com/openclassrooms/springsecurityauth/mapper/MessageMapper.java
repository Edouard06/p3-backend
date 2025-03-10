package com.openclassrooms.springsecurityauth.mapper;

import org.springframework.stereotype.Component;

import com.openclassrooms.springsecurityauth.dto.MessageDTO;
import com.openclassrooms.springsecurityauth.model.Message;

@Component
public class MessageMapper {
	
	public MessageDTO toDto(Message entity) {
		MessageDTO dto = new MessageDTO();
		dto.setMessage(entity.getMessage());
		return dto;
	}
	public Message toEntity(MessageDTO dto) {
		Message entity = new Message();
		entity.setMessage(dto.getMessage());
		return entity;
	}

}