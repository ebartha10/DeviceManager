package com.platform.device.services;

import com.platform.device.config.RabbitMQConfig;
import com.platform.device.dtos.AdminMessageRequest;
import com.platform.device.dtos.ChatMessageRequest;
import com.platform.device.dtos.ChatMessageResponse;
import com.platform.device.messaging.ChatRequestMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ChatRequestListener {

    private final ChatService chatService;

    public ChatRequestListener(ChatService chatService) {
        this.chatService = chatService;
    }

    @RabbitListener(queues = RabbitMQConfig.CHAT_REQUESTS_QUEUE)
    public void handleChatRequest(ChatRequestMessage message) {
        if ("ADMIN".equalsIgnoreCase(message.getRole())) {
            if (message.getTicketId() == null) {
                return;
            }
            AdminMessageRequest adminRequest = new AdminMessageRequest();
            adminRequest.setTicketId(message.getTicketId());
            adminRequest.setMessage(message.getMessage());
            chatService.sendAdminMessage(adminRequest);
        } else {
            // Default to USER if role is missing or USER
            if (message.getUserId() == null) {
                return;
            }
            ChatMessageRequest request = new ChatMessageRequest();
            request.setMessage(message.getMessage());
            chatService.processMessage(message.getUserId(), request);
        }
    }
}

