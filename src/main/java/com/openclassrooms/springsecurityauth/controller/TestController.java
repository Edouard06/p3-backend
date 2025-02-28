package com.openclassrooms.springsecurityauth.controller;

import com.openclassrooms.springsecurityauth.model.User;
import com.openclassrooms.springsecurityauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/test-db")
    public List<User> testDb() {
        return userRepository.findAll();
    }
}
