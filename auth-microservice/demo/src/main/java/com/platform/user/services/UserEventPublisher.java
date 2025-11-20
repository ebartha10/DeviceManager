package com.platform.user.services;

import com.platform.user.config.RabbitMQConfig;
import com.platform.user.messaging.UserEventMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserEventPublisher {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserEventPublisher.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publishUserCreated(UUID userId, String email, String fullName) {
        UserEventMessage message = new UserEventMessage("CREATE", userId, email, fullName);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.USER_EVENTS_EXCHANGE,
                RabbitMQConfig.USER_CREATE_ROUTING_KEY,
                message
        );
        LOGGER.info("Published user creation event for user {} to unified exchange", userId);
    }
}

