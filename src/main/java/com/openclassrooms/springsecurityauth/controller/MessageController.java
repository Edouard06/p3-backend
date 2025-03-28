package com.openclassrooms.springsecurityauth.controller;

import com.openclassrooms.springsecurityauth.dto.MessageDTO;
import com.openclassrooms.springsecurityauth.mapper.MessageMapper;
import com.openclassrooms.springsecurityauth.model.Message;
import com.openclassrooms.springsecurityauth.service.MessageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
@SecurityRequirement(name = "Bearer Authentication")
public class MessageController {

    private final MessageService messageService;
    private final MessageMapper messageMapper;

    public MessageController(MessageService messageService, MessageMapper messageMapper) {
        this.messageService = messageService;
        this.messageMapper = messageMapper;
    }

    @PostMapping
    public MessageDTO createMessage(@RequestBody MessageDTO dto) {
        Message message = messageMapper.toEntity(dto);
        Message saved = messageService.createMessage(message);
        return messageMapper.toDto(saved);
    }
}
