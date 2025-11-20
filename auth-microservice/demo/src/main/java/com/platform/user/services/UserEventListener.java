package com.platform.user.services;

import com.platform.user.config.RabbitMQConfig;
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

    @RabbitListener(queues = RabbitMQConfig.AUTH_USER_QUEUE)
    public void handleUserDeleted(UserEventMessage message) {
        LOGGER.info("Received user deletion event for user {} from user microservice", message.getUserId());
        
        if ("DELETE".equals(message.getEventType())) {
            UUID userId = message.getUserId();
            
            Optional<User> existingUser = userRepository.findById(userId);
            if (existingUser.isEmpty()) {
                LOGGER.info("User {} does not exist in auth microservice, skipping deletion", userId);
                return;
            }
            
            userService.deleteUser(userId);
            LOGGER.info("Successfully deleted user {} from auth microservice", userId);
        }
    }
}

