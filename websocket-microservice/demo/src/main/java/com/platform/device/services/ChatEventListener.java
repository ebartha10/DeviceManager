package com.platform.device.services;

import com.platform.device.config.RabbitMQConfig;
import com.platform.device.dtos.ChatWebSocketMessage;
import com.platform.device.messaging.ChatEventMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatEventListener {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatEventListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.WEBSOCKET_CHAT_QUEUE)
    public void handleChatEvent(ChatEventMessage event) {
        try {
            if (event == null || event.getTicketId() == null || event.getUserId() == null) {
                return;
            }

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

            // Also send to admin topic if sender is admin, user, or bot
            if ("admin".equals(event.getSender()) || "user".equals(event.getSender()) || "bot".equals(event.getSender())) {
                String adminTopic = "/topic/chat/admin";
                messagingTemplate.convertAndSend(adminTopic, wsMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
