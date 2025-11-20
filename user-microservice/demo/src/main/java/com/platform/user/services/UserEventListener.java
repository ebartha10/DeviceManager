package com.platform.user.services;

import com.platform.user.config.RabbitMQConfig;
import com.platform.user.dtos.UserDTO;
import com.platform.user.entities.User;
import com.platform.user.messaging.UserEventMessage;
import com.platform.user.repositories.UserRepository;
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

    @RabbitListener(queues = RabbitMQConfig.USER_MICROSERVICE_QUEUE)
    public void handleUserCreated(UserEventMessage message) {
        LOGGER.info("Received user creation event for user {} from auth microservice", message.getUserId());
        
        if ("CREATE".equals(message.getEventType())) {
            UUID userId = message.getUserId();
            String email = message.getEmail();
            
            Optional<User> existingUserById = userRepository.findById(userId);
            if (existingUserById.isPresent()) {
                LOGGER.info("User {} already exists in user microservice, skipping duplicate message", userId);
                return;
            }
            
            boolean userExistsByEmail = userRepository.findByEmail(email).isPresent();
            
            if (userExistsByEmail) {
                LOGGER.info("User with email {} already exists in user microservice, skipping duplicate message", email);
                return;
            }
            
            UserDTO userDTO = new UserDTO(userId, message.getFullName(), email);
            userService.createUser(userDTO, false);
            LOGGER.info("Successfully created user {} in user microservice", userId);
        }
    }
}

