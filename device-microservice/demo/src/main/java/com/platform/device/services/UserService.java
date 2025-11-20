package com.platform.device.services;

import com.platform.device.dtos.UserDTO;
import com.platform.device.dtos.builders.UserBuilder;
import com.platform.device.entities.User;
import com.platform.device.handlers.exceptions.model.ResourceNotFoundException;
import com.platform.device.repositories.UserDeviceRepository;
import com.platform.device.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDeviceRepository userDeviceRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${user-microservice.base-url}")
    private String userServiceBaseUrl;

    public UserDTO getUserById(UUID id) {
        Optional<User> user = this.userRepository.findById(id);

        if(user.isEmpty()) {
            LOGGER.error("User with id {} not found in user microservice", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }

        return UserBuilder.fromPersistence(user.get());
    }

    public UserDTO createUser(UserDTO userDTO) {
        User newUser = UserBuilder.toEntity(userDTO);

        newUser = this.userRepository.save(newUser);
        LOGGER.debug("User with id {} was inserted in db", newUser.getId());

        return UserBuilder.fromPersistence(newUser);
    }

    public void deleteUser(UUID userId) {
        Optional<User> user = this.userRepository.findById(userId);
        if(user.isEmpty()) {
            LOGGER.warn("User with id {} not found in device microservice, skipping deletion", userId);
            return;
        }

        userDeviceRepository.findByIdUserId(userId).forEach(userDevice -> {
            userDeviceRepository.delete(userDevice);
            LOGGER.debug("Deleted user-device relationship for user {} and device {}", 
                    userId, userDevice.getId().getDeviceId());
        });

        this.userRepository.deleteById(userId);
        LOGGER.debug("User with id {} was deleted from device microservice", userId);
    }
}
