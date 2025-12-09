import { useState, useEffect } from "react";
import { Box, Button, Typography } from "@mui/material";
import {
  Chat as ChatIcon,
  Close as CloseIcon,
  SupportAgent as SupportIcon,
} from "@mui/icons-material";
import { chatApi, type SupportTicket } from "../../api/chatApi";
import { chatWebSocketClient, type ChatWebSocketMessage } from "../../api/chatWebSocketClient";
import { AdminChat } from "./AdminChat";
import {
  ChatFloatingButton,
  ChatWindowContainer,
  ChatHeader,
  ChatTitle,
  ChatCloseButton,
  ChatMessagesArea,
} from "../dashboard/StyledComponents";

export function AdminChatPanel() {
  const [isOpen, setIsOpen] = useState(false);
  const [pendingTickets, setPendingTickets] = useState<SupportTicket[]>([]);
  const [activeTickets, setActiveTickets] = useState<SupportTicket[]>([]);
  const [selectedTicket, setSelectedTicket] = useState<SupportTicket | null>(null);
  const [unsubscribeRef, setUnsubscribeRef] = useState<(() => void) | null>(null);

  useEffect(() => {
    if (!isOpen) return;

    const loadAndSubscribe = async () => {
      await loadTickets();
      
      // Subscribe to WebSocket for new tickets
      try {
        await chatWebSocketClient.connect();
        const unsubscribe = chatWebSocketClient.subscribeToAdminChat(
          (wsMessage: ChatWebSocketMessage) => {
            // Refresh tickets when new messages arrive
            loadTickets();
          }
        );
        setUnsubscribeRef(() => unsubscribe);
      } catch (error) {
        console.error("Failed to connect admin WebSocket:", error);
      }
    };

    loadAndSubscribe();

    return () => {
      if (unsubscribeRef) {
        unsubscribeRef();
        setUnsubscribeRef(null);
      }
    };
  }, [isOpen]);

  const loadTickets = async () => {
    try {
      const [pending, active] = await Promise.all([
        chatApi.getPendingTickets(),
        chatApi.getActiveTickets(),
      ]);
      setPendingTickets(pending);
      setActiveTickets(active);
    } catch (error) {
      console.error("Failed to load tickets:", error);
    }
  };

  const handleTicketClick = async (ticketId: string) => {
    try {
      const ticket = await chatApi.getTicket(ticketId);
      setSelectedTicket(ticket);
    } catch (error) {
      console.error("Failed to load ticket:", error);
    }
  };

  if (selectedTicket) {
    return (
      <AdminChat 
        ticket={selectedTicket} 
        onClose={() => setSelectedTicket(null)} 
      />
    );
  }

  return (
    <>
      {isOpen && (
        <ChatWindowContainer elevation={3} sx={{ width: "400px", height: "600px" }}>
          <ChatHeader>
            <Box sx={{ display: "flex", alignItems: "center", gap: "8px" }}>
              <SupportIcon sx={{ color: "#00ffff" }} />
              <ChatTitle>ADMIN SUPPORT</ChatTitle>
            </Box>
            <ChatCloseButton onClick={() => setIsOpen(false)}>
              <CloseIcon />
            </ChatCloseButton>
          </ChatHeader>

          <ChatMessagesArea sx={{ overflowY: "auto", p: 2 }}>
            <Typography variant="h6" sx={{ color: "#00ffff", mb: 2 }}>
              Pending Tickets ({pendingTickets.length})
            </Typography>
            {pendingTickets.length === 0 ? (
              <Typography sx={{ color: "rgba(255,255,255,0.6)", mb: 3 }}>
                No pending tickets
              </Typography>
            ) : (
              pendingTickets.map((ticket) => (
                <Button
                  key={ticket.ticketId}
                  fullWidth
                  variant="outlined"
                  onClick={() => handleTicketClick(ticket.ticketId)}
                  sx={{
                    mb: 1,
                    color: "#ff00ff",
                    borderColor: "#ff00ff",
                    "&:hover": {
                      borderColor: "#ff00ff",
                      backgroundColor: "rgba(255, 0, 255, 0.1)",
                    },
                  }}
                >
                  Ticket {ticket.ticketId.substring(0, 8)} - User {ticket.userId.substring(0, 8)}
                </Button>
              ))
            )}

            <Typography variant="h6" sx={{ color: "#00ffff", mb: 2, mt: 3 }}>
              Active Tickets ({activeTickets.length})
            </Typography>
            {activeTickets.length === 0 ? (
              <Typography sx={{ color: "rgba(255,255,255,0.6)" }}>
                No active tickets
              </Typography>
            ) : (
              activeTickets.map((ticket) => (
                <Button
                  key={ticket.ticketId}
                  fullWidth
                  variant="outlined"
                  onClick={() => handleTicketClick(ticket.ticketId)}
                  sx={{
                    mb: 1,
                    color: "#00ffff",
                    borderColor: "#00ffff",
                    "&:hover": {
                      borderColor: "#00ffff",
                      backgroundColor: "rgba(0, 255, 255, 0.1)",
                    },
                  }}
                >
                  Ticket {ticket.ticketId.substring(0, 8)} - User {ticket.userId.substring(0, 8)}
                </Button>
              ))
            )}
          </ChatMessagesArea>
        </ChatWindowContainer>
      )}

      <ChatFloatingButton onClick={() => setIsOpen(!isOpen)}>
        {isOpen ? <CloseIcon /> : <ChatIcon />}
      </ChatFloatingButton>
    </>
  );
}

