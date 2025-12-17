package com.platform.device.controllers;

import com.platform.device.config.RabbitMQConfig;
import com.platform.device.messaging.ChatRequestMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    private final RabbitTemplate rabbitTemplate;

    public ChatWebSocketController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @MessageMapping("/sendMessage")
    public void sendMessage(ChatRequestMessage message) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.CHAT_REQUESTS_EXCHANGE,
            RabbitMQConfig.CHAT_REQUEST_ROUTING_KEY,
            message
        );
    }
}

