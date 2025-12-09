package com.platform.device.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatWebSocketMessage {
    private String ticketId;
    private String userId;
    private String sender; // "user", "bot", "admin"
    private String text;
    private String timestamp;
}

