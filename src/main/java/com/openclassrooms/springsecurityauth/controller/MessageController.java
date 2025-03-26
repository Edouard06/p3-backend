package com.openclassrooms.springsecurityauth.controller;

import com.openclassrooms.springsecurityauth.model.Message;
import com.openclassrooms.springsecurityauth.service.MessageService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/messages")
@SecurityRequirement(name = "Bearer Authentication") // ← On précise ici que ce contrôleur requiert un JWT
public class MessageController {

    private final MessageService messageService;

    /**
     * Constructs a new MessageController with the provided MessageService.
     *
     * @param messageService 
     */
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * Creates a new message.
     *
     * @param message 
     * @return 
     */
    @PostMapping("/message")
    public Message createMessage(@RequestBody Message message) {
        return messageService.createMessage(message);
    }
}
