package com.platform.device.repositories;

import com.platform.device.entities.SupportTicket;
import com.platform.device.entities.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, UUID> {
    List<SupportTicket> findByUserIdAndStatusNot(UUID userId, TicketStatus status);
    
    List<SupportTicket> findByStatus(TicketStatus status);
    
    Optional<SupportTicket> findByTicketId(UUID ticketId);
    
    default Optional<SupportTicket> findByUserIdAndIsResolvedFalse(UUID userId) {
        // Find tickets that are not RESOLVED
        List<SupportTicket> tickets = findByUserIdAndStatusNot(userId, TicketStatus.RESOLVED);
        return tickets.stream().findFirst();
    }
}

