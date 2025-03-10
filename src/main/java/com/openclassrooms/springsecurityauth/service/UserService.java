package com.openclassrooms.springsecurityauth.service;

import com.openclassrooms.springsecurityauth.dto.RegisterDTO;
import com.openclassrooms.springsecurityauth.dto.UserDTO;
import com.openclassrooms.springsecurityauth.exceptions.UserAlreadyExistException;
import com.openclassrooms.springsecurityauth.mapper.UserMapper;
import com.openclassrooms.springsecurityauth.model.User;
import com.openclassrooms.springsecurityauth.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDTO getUser(final int id) {
        Optional<User> optionalUser = userRepository.findById(Long.valueOf(id));
        User user = optionalUser.orElse(null);
        return userMapper.userToUserDTO(user);
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return user != null ? userMapper.userToUserDTO(user) : null;
    }
    
    public void updateUsername(int id, String newUsername) {
        Optional<User> optionalUser = userRepository.findById(Long.valueOf(id));
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUsername(newUsername);
            userRepository.save(user);
        }
    }
    
    public User saveUser(User user) {
        return userRepository.save(user);
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public User getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElse(null);
    }
    
    public User updateUser(Long id, User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEmail(updatedUser.getEmail());
            user.setUsername(updatedUser.getUsername());
            user.setPassword(updatedUser.getPassword());
            return userRepository.save(user);
        }
        return null;
    }
    
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void save(RegisterDTO registerDto) throws UserAlreadyExistException {
        // Vérifier si un utilisateur avec cet email existe déjà
        if (userRepository.findByEmail(registerDto.getEmail()) != null) {
            throw new UserAlreadyExistException("User with email " + registerDto.getEmail() + " already exists");
        }
        // Création d'un nouvel utilisateur à partir des données de RegisterDTO
        User user = new User();
        user.setEmail(registerDto.getEmail());
        user.setUsername(registerDto.getName()); // RegisterDTO doit fournir getName()
        user.setPassword(registerDto.getPassword());
        userRepository.save(user);
    }
}
