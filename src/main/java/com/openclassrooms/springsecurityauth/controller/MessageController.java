package com.openclassrooms.springsecurityauth.controller;

import com.openclassrooms.springsecurityauth.model.Message;
import com.openclassrooms.springsecurityauth.service.MessageService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/messages")
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
