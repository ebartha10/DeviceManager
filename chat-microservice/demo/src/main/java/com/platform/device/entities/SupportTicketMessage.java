package com.platform.device.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "support_ticket_messages")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupportTicketMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID messageId;

    private UUID ticketId;

    private String sender; // "user", "admin", "bot"

    private String messageContent;

    private LocalDateTime timestamp;
}
