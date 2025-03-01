package com.openclassrooms.springsecurityauth.service;

import com.openclassrooms.springsecurityauth.model.Message;
import com.openclassrooms.springsecurityauth.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }
}
