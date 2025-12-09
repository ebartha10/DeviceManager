package com.platform.device.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageRequest {
    private String message;
    // In a real scenario, userId would come from the JWT token in the security context
    // For this demo, we might need to extract it or have it passed. 
    // Since the frontend prompt implies a logged-in user, we'll assume we can get it.
}

