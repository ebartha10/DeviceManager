package com.platform.device.controllers;

import com.platform.device.dtos.AdminMessageRequest;
import com.platform.device.dtos.ChatMessageRequest;
import com.platform.device.dtos.ChatMessageResponse;
import com.platform.device.dtos.SupportTopicDTO;
import com.platform.device.dtos.SupportTicketDTO;
import com.platform.device.services.ChatService;
import com.platform.device.services.SupportTopicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = "*") // Allow frontend access
public class ChatController {

    private final ChatService chatService;
    private final SupportTopicService supportTopicService;

    public ChatController(ChatService chatService, SupportTopicService supportTopicService) {
        this.chatService = chatService;
        this.supportTopicService = supportTopicService;
    }

    @GetMapping("/topics")
    public ResponseEntity<List<SupportTopicDTO>> getSupportTopics() {
        return ResponseEntity.ok(supportTopicService.getSupportTopics());
    }

    @PostMapping("/send")
    public ResponseEntity<ChatMessageResponse> sendMessage(
            @RequestHeader(value = "X-User-Id", defaultValue = "3d2d9056-ef50-4387-aa89-f147d432ba21") String userIdStr,
            @RequestBody ChatMessageRequest request) {
        
        UUID userId;
        try {
            userId = UUID.fromString(userIdStr);
        } catch (IllegalArgumentException e) {
            userId = UUID.fromString("3d2d9056-ef50-4387-aa89-f147d432ba21");
        }

        ChatMessageResponse response = chatService.processMessage(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/pending")
    public ResponseEntity<List<SupportTicketDTO>> getPendingTickets() {
        return ResponseEntity.ok(chatService.getPendingTickets());
    }

    @GetMapping("/admin/active")
    public ResponseEntity<List<SupportTicketDTO>> getActiveTickets() {
        return ResponseEntity.ok(chatService.getActiveTickets());
    }

    @GetMapping("/admin/ticket/{ticketId}")
    public ResponseEntity<SupportTicketDTO> getTicket(@PathVariable UUID ticketId) {
        return ResponseEntity.ok(chatService.getTicketById(ticketId));
    }

    @PostMapping("/admin/send")
    public ResponseEntity<ChatMessageResponse> sendAdminMessage(@RequestBody AdminMessageRequest request) {
        ChatMessageResponse response = chatService.sendAdminMessage(request);
        return ResponseEntity.ok(response);
    }
}

