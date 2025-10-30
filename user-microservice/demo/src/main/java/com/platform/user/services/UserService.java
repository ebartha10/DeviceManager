package com.platform.user.services;

import com.platform.user.dtos.UserDTO;
import com.platform.user.dtos.builders.UserBuilder;
import com.platform.user.entities.User;
import com.platform.user.handlers.exceptions.model.ResourceNotFoundException;
import com.platform.user.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${device-microservice.base-url}")
    private String deviceServiceBaseUrl;

    public List<UserDTO> getAllUsers() {
        List<User> users = (List<User>) this.userRepository.findAll();
        return users.stream().map(UserBuilder::fromPersistance).toList();
    }

    public UserDTO getUserById(UUID id) {
        Optional<User> user = this.userRepository.findById(id);
        if(user.isEmpty()) {
            LOGGER.error("User with id {} was not found in db", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }

        return UserBuilder.fromPersistance(user.get());
    }

    public UserDTO createUser(UserDTO user) {
        User newUser = UserBuilder.toUserEntity(user);
        newUser = this.userRepository.save(newUser);
        LOGGER.debug("User with id {} was inserted in db", newUser.getId());
        
        try {
            String url = this.deviceServiceBaseUrl + "/users";
            Map<String, Object> body = Map.of("id", newUser.getId());
            this.restTemplate.postForLocation(url, body);
        } catch (Exception ex) {
            LOGGER.warn("Failed to propagate user {} to device microservice: {}", newUser.getId(), ex.getMessage());
        }
        return UserBuilder.fromPersistance(newUser);
    }

    public UserDTO updateUser(UserDTO givenUser) {
        Optional<User> user = this.userRepository.findById(givenUser.getId());
        if(user.isEmpty()) {
            LOGGER.error("User with id {} was not found in db", givenUser.getId());
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + givenUser.getId());
        }

        User existingUser = user.get();
        existingUser.setFullName(givenUser.getFullName());
        existingUser.setEmail(givenUser.getEmail());

        existingUser = userRepository.save(existingUser);
        LOGGER.debug("User with id {} was updated in db", existingUser.getId());

        return UserBuilder.fromPersistance(existingUser);
    }

    public void deleteById(UUID id) {
        Optional<User> user = this.userRepository.findById(id);
        if(user.isEmpty()) {
            LOGGER.error("User with id {} was not found in db", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }

        this.userRepository.deleteById(id);
        LOGGER.debug("User with id {} was deleted from db", id);
    }

}