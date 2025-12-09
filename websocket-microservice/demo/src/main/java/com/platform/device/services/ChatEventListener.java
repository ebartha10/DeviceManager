package com.platform.device.services;

import com.platform.device.config.RabbitMQConfig;
import com.platform.device.dtos.ChatWebSocketMessage;
import com.platform.device.messaging.ChatEventMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatEventListener.class);

    private final SimpMessagingTemplate messagingTemplate;

    public ChatEventListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.WEBSOCKET_CHAT_QUEUE)
    public void handleChatEvent(ChatEventMessage event) {
        LOGGER.info("Received chat event for ticket {} from user {}", event.getTicketId(), event.getUserId());

        // Convert to WebSocket message format
        ChatWebSocketMessage wsMessage = new ChatWebSocketMessage();
        wsMessage.setTicketId(event.getTicketId().toString());
        wsMessage.setUserId(event.getUserId().toString());
        wsMessage.setSender(event.getSender());
        wsMessage.setText(event.getMessage());
        wsMessage.setTimestamp(event.getTimestamp());

        // Send to user-specific topic: /topic/chat/{userId}
        String userTopic = "/topic/chat/" + event.getUserId();
        messagingTemplate.convertAndSend(userTopic, wsMessage);
        LOGGER.info("Sent chat message to WebSocket topic: {}", userTopic);

        // Also send to admin topic if sender is admin or if admin needs to see it
        if ("admin".equals(event.getSender()) || "user".equals(event.getSender())) {
            String adminTopic = "/topic/chat/admin";
            messagingTemplate.convertAndSend(adminTopic, wsMessage);
            LOGGER.info("Sent chat message to admin WebSocket topic");
        }
    }
}

