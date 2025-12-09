package com.platform.device.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupportTicketDTO {
    private UUID ticketId;
    private UUID userId;
    private LocalDateTime createdAt;
    private String status;
    private List<SupportTicketMessageDTO> messages;
}

