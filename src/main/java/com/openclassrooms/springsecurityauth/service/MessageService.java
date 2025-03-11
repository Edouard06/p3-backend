package com.openclassrooms.springsecurityauth.service;

import com.openclassrooms.springsecurityauth.model.Message;
import com.openclassrooms.springsecurityauth.repository.MessageRepository;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }
}
