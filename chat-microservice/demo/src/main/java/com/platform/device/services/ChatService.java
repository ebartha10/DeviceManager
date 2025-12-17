package com.platform.device.services;

import com.platform.device.dtos.AdminMessageRequest;
import com.platform.device.dtos.ChatMessageRequest;
import com.platform.device.dtos.ChatMessageResponse;
import com.platform.device.dtos.SupportTicketDTO;
import com.platform.device.dtos.SupportTicketMessageDTO;
import com.platform.device.entities.SupportTicket;
import com.platform.device.entities.SupportTicketMessage;
import com.platform.device.entities.TicketStatus;
import com.platform.device.repositories.SupportTicketRepository;
import com.platform.device.repositories.SupportTicketMessageRepository;
import com.platform.device.config.RabbitMQConfig;
import com.platform.device.messaging.ChatEventMessage;
import com.platform.device.utils.ChatRules;
import com.platform.device.dtos.SupportTopicDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final SupportTicketRepository ticketRepository;
    private final SupportTicketMessageRepository messageRepository;
    private final RestTemplate restTemplate;
    private final RabbitTemplate rabbitTemplate;
    private final SupportTopicService supportTopicService;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    public ChatService(SupportTicketRepository ticketRepository, 
                      SupportTicketMessageRepository messageRepository,
                      RabbitTemplate rabbitTemplate,
                      SupportTopicService supportTopicService) {
        this.ticketRepository = ticketRepository;
        this.messageRepository = messageRepository;
        this.restTemplate = new RestTemplate();
        this.rabbitTemplate = rabbitTemplate;
        this.supportTopicService = supportTopicService;
    }

    public ChatMessageResponse processMessage(UUID userId, ChatMessageRequest request) {
        // 1. Find active ticket
        SupportTicket ticket = ticketRepository.findByUserIdAndIsResolvedFalse(userId)
                .orElseGet(() -> createNewTicket(userId));

        // 2. Check for admin handoff request
        if (request.getMessage().toLowerCase().contains("admin") || 
            request.getMessage().toLowerCase().contains("human")) {
            ticket.setStatus(TicketStatus.PENDING_ADMIN);
            ticketRepository.save(ticket);
            saveMessage(ticket.getTicketId(), "user", request.getMessage());
            publishChatEvent(ticket.getTicketId(), userId, "user", request.getMessage());
            
            String response = "I have notified a human agent. They will join the chat shortly.";
            saveMessage(ticket.getTicketId(), "bot", response);
            publishChatEvent(ticket.getTicketId(), userId, "bot", response);
            return new ChatMessageResponse("bot", response, LocalDateTime.now().toString());
        }

        // 3. If ticket is already pending admin or in progress with admin
        if (ticket.getStatus() == TicketStatus.PENDING_ADMIN || ticket.getStatus() == TicketStatus.IN_PROGRESS_ADMIN) {
            saveMessage(ticket.getTicketId(), "user", request.getMessage());
            publishChatEvent(ticket.getTicketId(), userId, "user", request.getMessage());
            return new ChatMessageResponse("system", "Message sent. Waiting for agent...", LocalDateTime.now().toString());
        }

        // 4. Save user message (Bot Mode)
        saveMessage(ticket.getTicketId(), "user", request.getMessage());
        publishChatEvent(ticket.getTicketId(), userId, "user", request.getMessage());

        // 5. Check for predefined support topic responses first
        String supportTopicResponse = getSupportTopicResponse(request.getMessage());
        if (supportTopicResponse != null) {
            saveMessage(ticket.getTicketId(), "bot", supportTopicResponse);
            publishChatEvent(ticket.getTicketId(), userId, "bot", supportTopicResponse);
            return new ChatMessageResponse("bot", supportTopicResponse, LocalDateTime.now().toString());
        }

        // 6. Try Rule-based Response
        String ruleResponse = ChatRules.getResponse(request.getMessage());
        if (ruleResponse != null) {
            saveMessage(ticket.getTicketId(), "bot", ruleResponse);
            publishChatEvent(ticket.getTicketId(), userId, "bot", ruleResponse);
            return new ChatMessageResponse("bot", ruleResponse, LocalDateTime.now().toString());
        }

        // 7. Fallback to Gemini (AI)
        String geminiResponse = callGemini(request.getMessage());
        saveMessage(ticket.getTicketId(), "bot", geminiResponse);
        publishChatEvent(ticket.getTicketId(), userId, "bot", geminiResponse);

        return new ChatMessageResponse("bot", geminiResponse, LocalDateTime.now().toString());
    }

    public List<SupportTicketDTO> getPendingTickets() {
        List<SupportTicket> tickets = ticketRepository.findByStatus(TicketStatus.PENDING_ADMIN);
        return tickets.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<SupportTicketDTO> getActiveTickets() {
        List<SupportTicket> tickets = ticketRepository.findByStatus(TicketStatus.IN_PROGRESS_ADMIN);
        return tickets.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public SupportTicketDTO getTicketById(UUID ticketId) {
        return ticketRepository.findByTicketId(ticketId)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    public ChatMessageResponse sendAdminMessage(AdminMessageRequest request) {
        SupportTicket ticket = ticketRepository.findByTicketId(request.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        // Update ticket status if needed
        if (ticket.getStatus() == TicketStatus.PENDING_ADMIN) {
            ticket.setStatus(TicketStatus.IN_PROGRESS_ADMIN);
            ticketRepository.save(ticket);
        }

        // Save admin message
        saveMessage(ticket.getTicketId(), "admin", request.getMessage());
        publishChatEvent(ticket.getTicketId(), ticket.getUserId(), "admin", request.getMessage());

        return new ChatMessageResponse("admin", request.getMessage(), LocalDateTime.now().toString());
    }

    private SupportTicketDTO convertToDTO(SupportTicket ticket) {
        SupportTicketDTO dto = new SupportTicketDTO();
        dto.setTicketId(ticket.getTicketId());
        dto.setUserId(ticket.getUserId());
        dto.setCreatedAt(ticket.getCreatedAt());
        dto.setStatus(ticket.getStatus().toString());
        
        List<SupportTicketMessage> messages = messageRepository.findByTicketIdOrderByTimestampAsc(ticket.getTicketId());
        List<SupportTicketMessageDTO> messageDTOs = messages.stream().map(msg -> {
            SupportTicketMessageDTO msgDTO = new SupportTicketMessageDTO();
            msgDTO.setSender(msg.getSender());
            msgDTO.setMessageContent(msg.getMessageContent());
            msgDTO.setTimestamp(msg.getTimestamp());
            return msgDTO;
        }).collect(Collectors.toList());
        
        dto.setMessages(messageDTOs);
        return dto;
    }

    private SupportTicket createNewTicket(UUID userId) {
        SupportTicket ticket = new SupportTicket();
        ticket.setUserId(userId);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setStatus(TicketStatus.BOT_MODE);
        return ticketRepository.save(ticket);
    }

    private void saveMessage(UUID ticketId, String sender, String content) {
        SupportTicketMessage message = new SupportTicketMessage();
        message.setTicketId(ticketId);
        message.setSender(sender);
        message.setMessageContent(content);
        message.setTimestamp(LocalDateTime.now());
        messageRepository.save(message);
    }

    private String getSupportTopicResponse(String message) {
        String lowerMsg = message.toLowerCase().trim();
        List<SupportTopicDTO> topics = supportTopicService.getSupportTopics();
        
        for (SupportTopicDTO topic : topics) {
            String topicLabelLower = topic.getLabel().toLowerCase().trim();
            // Check if message exactly matches the topic label (case-insensitive)
            if (lowerMsg.equals(topicLabelLower)) {
                return topic.getResponse();
            }
        }
        
        return null;
    }

    private void publishChatEvent(UUID ticketId, UUID userId, String sender, String message) {
        try {
            ChatEventMessage event = new ChatEventMessage();
            event.setTicketId(ticketId);
            event.setUserId(userId);
            event.setSender(sender);
            event.setMessage(message);
            event.setTimestamp(LocalDateTime.now().toString());
            
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.CHAT_EVENTS_EXCHANGE,
                RabbitMQConfig.CHAT_MESSAGE_ROUTING_KEY,
                event
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String callGemini(String userMessage) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-goog-api-key", geminiApiKey);
            
            // System instruction with platform context
            Map<String, String> systemInstructionPart = new HashMap<>();
            systemInstructionPart.put("text", 
                "You are a helpful support assistant for NEXUS, a device management and monitoring platform. " +
                "NEXUS allows users to monitor their IoT devices, track energy consumption in real-time, and manage their device fleet. " +
                "Key features include: " +
                "- Adding and managing multiple devices from a dashboard " +
                "- Real-time energy consumption monitoring with charts and graphs " +
                "- Device status tracking and alerts " +
                "- Free tier supports up to 5 devices " +
                "- Enterprise plans available for larger deployments " +
                "When helping users, be friendly, concise, and focus on practical solutions. " +
                "If a question is about device connectivity, suggest checking power, Wi-Fi range, or restarting the device. " +
                "For technical issues beyond your knowledge, direct users to contact support@nexus.com. " +
                "Always maintain a professional and helpful tone."
            );
            Map<String, Object> systemInstruction = new HashMap<>();
            systemInstruction.put("parts", Collections.singletonList(systemInstructionPart));
            
            // User message content
            Map<String, String> contentPart = new HashMap<>();
            contentPart.put("text", userMessage);
            Map<String, Object> content = new HashMap<>();
            content.put("parts", Collections.singletonList(contentPart));
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("system_instruction", systemInstruction);
            requestBody.put("contents", Collections.singletonList(content));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<Map> response = restTemplate.postForEntity(geminiApiUrl, entity, Map.class);
            
            if (response.getBody() != null && response.getBody().containsKey("candidates")) {
                java.util.List candidates = (java.util.List) response.getBody().get("candidates");
                if (!candidates.isEmpty()) {
                    Map candidate = (Map) candidates.get(0);
                    Map contentResponse = (Map) candidate.get("content");
                    java.util.List partsResponse = (java.util.List) contentResponse.get("parts");
                    if (!partsResponse.isEmpty()) {
                        Map part = (Map) partsResponse.get(0);
                        return (String) part.get("text");
                    }
                }
            }
            
            return "I received your message but couldn't understand the response.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Sorry, I'm having trouble connecting to the AI service.";
        }
    }
}