import { useState, useEffect, useRef } from "react";
import { Box, Button, Typography } from "@mui/material";
import {
  Chat as ChatIcon,
  Close as CloseIcon,
  Send as SendIcon,
  Person as PersonIcon,
} from "@mui/icons-material";
import {
  ChatFloatingButton,
  ChatWindowContainer,
  ChatHeader,
  ChatTitle,
  ChatCloseButton,
  ChatMessagesArea,
  ChatMessageBubble,
  ChatInputContainer,
  ChatInput,
  SendButton,
} from "../dashboard/StyledComponents";
import { chatApi, type SupportTicket } from "../../api/chatApi";
import { chatWebSocketClient, type ChatWebSocketMessage } from "../../api/chatWebSocketClient";

interface Message {
  id: string;
  text: string;
  sender: "user" | "bot" | "admin";
  timestamp: Date;
}

interface AdminChatProps {
  ticket: SupportTicket;
  onClose: () => void;
}

export function AdminChat({ ticket, onClose }: AdminChatProps) {
  const [messages, setMessages] = useState<Message[]>([]);
  const [inputValue, setInputValue] = useState("");
  const [isSending, setIsSending] = useState(false);
  const messagesEndRef = useRef<HTMLDivElement>(null);
  const unsubscribeRef = useRef<(() => void) | null>(null);

  useEffect(() => {
    // Load existing messages
    const loadMessages = async () => {
      try {
        const ticketData = await chatApi.getTicket(ticket.ticketId);
        const loadedMessages: Message[] = ticketData.messages.map((msg, idx) => ({
          id: `${ticket.ticketId}-${idx}`,
          text: msg.messageContent,
          sender: msg.sender as "user" | "bot" | "admin",
          timestamp: new Date(msg.timestamp),
        }));
        setMessages(loadedMessages);
      } catch (error) {
        console.error("Failed to load ticket messages:", error);
      }
    };

    loadMessages();

    // Subscribe to WebSocket for real-time updates
    chatWebSocketClient
      .connect()
      .then(() => {
        // Subscribe to admin topic for all admin messages
        const unsubscribe = chatWebSocketClient.subscribeToAdminChat(
          (wsMessage: ChatWebSocketMessage) => {
            // Only add messages for this ticket
            if (wsMessage.ticketId === ticket.ticketId) {
              setMessages((prev) => {
                const messageId = `${wsMessage.ticketId}-${wsMessage.timestamp}`;
                const wsTimestamp = new Date(wsMessage.timestamp).getTime();
                
                // Check for duplicate by ID
                const existsById = prev.some((m) => m.id === messageId);
                if (existsById) return prev;

                // Check for duplicate by content, sender, and timestamp (within 1 second)
                // This catches exact duplicates even if IDs differ (e.g., optimistic updates)
                const isDuplicate = prev.some((m) => 
                  m.text === wsMessage.text &&
                  m.sender === wsMessage.sender &&
                  Math.abs(new Date(m.timestamp).getTime() - wsTimestamp) < 1000
                );
                
                if (isDuplicate) {
                  console.log("Duplicate admin message detected, skipping:", wsMessage.text);
                  return prev;
                }

                // Check for optimistic admin message to replace
                if (wsMessage.sender === "admin") {
                  const optimisticIndex = prev.findIndex((m) => 
                    m.sender === "admin" &&
                    m.text === wsMessage.text &&
                    m.id.startsWith("temp-")
                  );
                  
                  if (optimisticIndex !== -1) {
                    // Replace optimistic message with real WebSocket message
                    const updated = [...prev];
                    updated[optimisticIndex] = {
                      id: messageId,
                      text: wsMessage.text,
                      sender: wsMessage.sender as "user" | "bot" | "admin",
                      timestamp: new Date(wsMessage.timestamp),
                    };
                    return updated;
                  }
                }

                return [
                  ...prev,
                  {
                    id: messageId,
                    text: wsMessage.text,
                    sender: wsMessage.sender as "user" | "bot" | "admin",
                    timestamp: new Date(wsMessage.timestamp),
                  },
                ];
              });
            }
          }
        );
        unsubscribeRef.current = unsubscribe;
      })
      .catch((error) => {
        console.error("Failed to connect admin chat WebSocket:", error);
      });

    return () => {
      if (unsubscribeRef.current) {
        unsubscribeRef.current();
        unsubscribeRef.current = null;
      }
    };
  }, [ticket.ticketId]);

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  const handleSendMessage = async () => {
    const messageText = inputValue.trim();
    if (!messageText || isSending) return;

    setInputValue("");
    setIsSending(true);

    // Add admin message optimistically
    const adminMsg: Message = {
      id: `temp-${Date.now()}`,
      text: messageText,
      sender: "admin",
      timestamp: new Date(),
    };
    setMessages((prev) => [...prev, adminMsg]);

    try {
      // Send to backend - response will come via WebSocket
      await chatApi.sendAdminMessage(ticket.ticketId, messageText);
    } catch (error) {
      console.error("Failed to send admin message:", error);
      // Remove optimistic message on error
      setMessages((prev) => prev.filter((m) => m.id !== adminMsg.id));
      const errorMsg: Message = {
        id: (Date.now() + 1).toString(),
        text: "Failed to send message. Please try again.",
        sender: "admin",
        timestamp: new Date(),
      };
      setMessages((prev) => [...prev, errorMsg]);
    } finally {
      setIsSending(false);
    }
  };

  const handleKeyPress = (e: React.KeyboardEvent) => {
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault();
      handleSendMessage();
    }
  };

  return (
    <ChatWindowContainer elevation={3} sx={{ width: "500px", height: "600px" }}>
      <ChatHeader>
        <Box sx={{ display: "flex", alignItems: "center", gap: "8px" }}>
          <PersonIcon sx={{ color: "#00ffff" }} />
          <ChatTitle>Support Ticket - {ticket.ticketId.substring(0, 8)}</ChatTitle>
        </Box>
        <ChatCloseButton onClick={onClose}>
          <CloseIcon />
        </ChatCloseButton>
      </ChatHeader>

      <ChatMessagesArea>
        {messages.map((msg) => (
          <ChatMessageBubble 
            key={msg.id} 
            isUser={msg.sender === "admin"}
            sx={{
              alignSelf: msg.sender === "admin" ? "flex-end" : "flex-start",
              background: msg.sender === "admin" 
                ? "rgba(0, 255, 255, 0.2)" 
                : msg.sender === "bot"
                ? "rgba(255, 0, 255, 0.2)"
                : "rgba(255, 255, 0, 0.2)",
            }}
          >
            {msg.text}
          </ChatMessageBubble>
        ))}
        <div ref={messagesEndRef} />
      </ChatMessagesArea>

      <ChatInputContainer>
        <ChatInput
          placeholder="Type your response..."
          variant="outlined"
          size="small"
          value={inputValue}
          onChange={(e) => setInputValue(e.target.value)}
          onKeyDown={handleKeyPress}
          disabled={isSending}
        />
        <SendButton 
          onClick={handleSendMessage}
          disabled={!inputValue.trim() || isSending}
        >
          <SendIcon />
        </SendButton>
      </ChatInputContainer>
    </ChatWindowContainer>
  );
}

