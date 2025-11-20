package com.platform.user.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEventMessage {
    private String eventType; // "CREATE" or "DELETE"
    private UUID userId;
    private String email;
    private String fullName;
}

