package com.openclassrooms.springsecurityauth.controller;

import com.openclassrooms.springsecurityauth.model.Message;
import com.openclassrooms.springsecurityauth.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    // POST /messages/message
    @PostMapping("/message")
    public Message createMessage(@RequestBody Message message) {
        return messageService.createMessage(message);
    }
}
