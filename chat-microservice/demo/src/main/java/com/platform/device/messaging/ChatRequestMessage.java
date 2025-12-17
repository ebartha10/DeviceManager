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
public class ChatRequestMessage {
    private UUID userId;
    private String message;
    private UUID ticketId;
    private String role; // user or admin
}
