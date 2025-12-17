package com.platform.device.messaging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatEventMessage {
    private UUID ticketId;
    private UUID userId;
    private String sender; // user, bot, admin
    private String message;
    private String timestamp;
}
