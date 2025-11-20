package com.platform.device.services;

import com.platform.device.config.RabbitMQConfig;
import com.platform.device.dtos.UserDTO;
import com.platform.device.entities.User;
import com.platform.device.messaging.UserEventMessage;
import com.platform.device.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserEventListener.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @RabbitListener(queues = RabbitMQConfig.MONITORING_USER_QUEUE)
    public void handleUserEvent(UserEventMessage message) {
        if ("CREATE".equals(message.getEventType())) {
            UUID userId = message.getUserId();
            LOGGER.info("Received user creation event for user {} from user microservice", userId);
            
            Optional<User> existingUser = userRepository.findById(userId);
            if (existingUser.isPresent()) {
                LOGGER.info("User {} already exists in device microservice, skipping duplicate message", userId);
                return;
            }
            
            UserDTO userDTO = new UserDTO();
            userDTO.setId(userId);
            userService.createUser(userDTO);
            LOGGER.info("Successfully created user {} in device microservice", userId);
            
        } else if ("DELETE".equals(message.getEventType())) {
            UUID userId = message.getUserId();
            LOGGER.info("Received user deletion event for user {} from user microservice", userId);
            
            Optional<User> existingUser = userRepository.findById(userId);
            if (existingUser.isEmpty()) {
                LOGGER.info("User {} does not exist in device microservice, skipping deletion", userId);
                return;
            }
            
            userService.deleteUser(userId);
            LOGGER.info("Successfully deleted user {} from device microservice", userId);
        }
    }
}

