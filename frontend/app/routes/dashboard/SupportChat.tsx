import { useState, useEffect, useRef } from "react";
import { Box, Button } from "@mui/material";
import {
  Chat as ChatIcon,
  Close as CloseIcon,
  SmartToy as BotIcon,
  Send as SendIcon,
} from "@mui/icons-material";
import {
  ChatFloatingButton,
  ChatWindowContainer,
  ChatHeader,
  ChatTitle,
  ChatCloseButton,
  ChatMessagesArea,
  ChatMessageBubble,
  ChatOptionsContainer,
  ChatOptionButton,
  ChatInputContainer,
  ChatInput,
  SendButton,
} from "./StyledComponents";
import { supportTopics } from "../../data/supportData";
import { chatApi } from "../../api/chatApi";
import { chatWebSocketClient, type ChatWebSocketMessage } from "../../api/chatWebSocketClient";
import { tokenStorage } from "../../api/tokenStorage";

interface Message {
  id: string;
  text: string;
  sender: "user" | "bot" | "admin";
  timestamp: Date;
}

export function SupportChat() {
  const [isOpen, setIsOpen] = useState(false);
  const [messages, setMessages] = useState<Message[]>([]);
  const [isLiveChat, setIsLiveChat] = useState(false);
  const [inputValue, setInputValue] = useState("");
  const [isSending, setIsSending] = useState(false);
  const [topics, setTopics] = useState(supportTopics);
  const messagesEndRef = useRef<HTMLDivElement>(null);
  const unsubscribeRef = useRef<(() => void) | null>(null);
  const pendingResponseTimeoutRef = useRef<NodeJS.Timeout | null>(null);

  useEffect(() => {
    const loadTopics = async () => {
      try {
        const data = await chatApi.getTopics();
        if (data && data.length > 0) {
          setTopics(data);
        }
      } catch (error) {
        console.error("Failed to load support topics:", error);
        // Fallback to static topics is automatic due to initial state
      }
    };
    
    if (isOpen) {
      loadTopics();
    }
  }, [isOpen]);

  useEffect(() => {
    if (isOpen && messages.length === 0) {
      // Initial greeting
      setMessages([
        {
          id: "init-1",
          text: "Hello! I'm the NEXUS Support Bot. How can I help you with your devices today?",
          sender: "bot",
          timestamp: new Date(),
        },
      ]);
    }
  }, [isOpen]);

  // WebSocket subscription for real-time messages
  useEffect(() => {
    if (!isOpen) {
      // Clean up subscription when closing
      if (unsubscribeRef.current) {
        unsubscribeRef.current();
        unsubscribeRef.current = null;
      }
      return;
    }
    // Subscribing always when open, regardless of isLiveChat, to receive responses for predefined topics too


    const userId = tokenStorage.getUserId();
    if (!userId) return;

    // Prevent multiple subscriptions
    if (unsubscribeRef.current) {
      return;
    }

    // Connect and subscribe to WebSocket
    chatWebSocketClient
      .connect()
      .then(() => {
        // Double-check we don't already have a subscription
        if (unsubscribeRef.current) {
          return;
        }
        
        const unsubscribe = chatWebSocketClient.subscribeToUserChat(
          userId,
          (wsMessage: ChatWebSocketMessage) => {
            console.log("[SupportChat] Received WebSocket message:", wsMessage);
            // Only add messages that aren't already in the list
            setMessages((prev) => {
              console.log("[SupportChat] Current messages count:", prev.length);
              const messageId = `${wsMessage.ticketId}-${wsMessage.timestamp}`;
              const wsTimestamp = new Date(wsMessage.timestamp).getTime();
              
              // Check for duplicate by ID
              const existsById = prev.some((m) => m.id === messageId);
              if (existsById) {
                console.log("[SupportChat] Duplicate by ID, skipping:", messageId);
                return prev;
              }

              // Check for duplicate by content, sender, and timestamp (within 1 second)
              // This catches exact duplicates even if IDs differ
              const isDuplicate = prev.some((m) => 
                m.text === wsMessage.text &&
                m.sender === wsMessage.sender &&
                Math.abs(new Date(m.timestamp).getTime() - wsTimestamp) < 1000
              );
              
              if (isDuplicate) {
                console.log("[SupportChat] Duplicate by content, skipping:", wsMessage.text);
                return prev;
              }

              // Check for optimistic user message to replace
              if (wsMessage.sender === "user") {
                const optimisticIndex = prev.findIndex((m) => 
                  m.sender === "user" &&
                  m.text === wsMessage.text &&
                  m.id.startsWith("temp-")
                );
                
                if (optimisticIndex !== -1) {
                  console.log("[SupportChat] Replacing optimistic user message");
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

              // Clear pending REST response timeout since WebSocket delivered
              if (pendingResponseTimeoutRef.current) {
                clearTimeout(pendingResponseTimeoutRef.current);
                pendingResponseTimeoutRef.current = null;
              }

              console.log("[SupportChat] Adding new message:", wsMessage.sender, wsMessage.text);
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
        );
        unsubscribeRef.current = unsubscribe;
      })
      .catch((error) => {
        console.error("Failed to connect chat WebSocket:", error);
      });

    return () => {
      if (unsubscribeRef.current) {
        unsubscribeRef.current();
        unsubscribeRef.current = null;
      }
    };
  }, [isOpen]); // Removed isLiveChat dependency

  useEffect(() => {
    scrollToBottom();
  }, [messages, isOpen]);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  const handleOptionClick = async (topicId: string) => {
    const topic = topics.find((t) => t.id === topicId);
    if (!topic) return;

    if (topicId === "other") {
      // Switch to live chat mode immediately
      setIsLiveChat(true);
      // Send the "other" message to backend - response will come via WebSocket
      await handleSendMessage(topic.label);
    } else {
      // For predefined topics, send to backend - response will come via WebSocket
      await handleSendMessage(topic.label);
    }
  };

  const handleSendMessage = async (overrideMessage?: string) => {
    const messageText = overrideMessage || inputValue.trim();
    if (!messageText || isSending) return;

    if (!overrideMessage) setInputValue("");
    setIsSending(true);

    // Add user message immediately (optimistic update) - removed to rely on WebSocket echo
    // This prevents duplicates and ensures the server processed it.
    let userMsg: Message | null = null;


    try {
      // Send via WebSocket (including for predefined topics)
      const userId = tokenStorage.getUserId();
      if (userId) {
        // If we are selecting a predefined topic (not live chat yet), 
        // we send the message via WebSocket. The backend will process it 
        // and return a response (e.g. predefined answer) via WebSocket.
        // However, if we're not in live chat, we might miss the response if we're not subscribed?
        // Wait, we are subscribed in useEffect if isOpen is true, but we added `!isLiveChat` condition to return early.
        // We need to ensure we are subscribed even if not isLiveChat if we want to receive responses for predefined topics.
        
        // Actually, the previous logic was: if !isLiveChat, use REST.
        // Now we want to use WebSocket for EVERYTHING.
        // So we must subscribe to UserChat when isOpen is true, regardless of isLiveChat.
        
        chatWebSocketClient.sendMessage(userId, messageText);
      } else {
        console.error("No user ID found for WebSocket message");
        throw new Error("No user ID found");
      }
      
      // Optimistic update for USER message (only if NOT in live chat to avoid duplicates if we decide to keep that logic?
      // Or just rely on WebSocket echo for everything to be consistent?)
      // The user previously said: "the sender sees his message twice... make it update after the static requests (without websocket)"
      // But now "I want to have websocket session... sends it back to the frontend".
      // This implies the backend will ECHO the user message back.
      // So we should NOT add optimistic update, and wait for the echo.
      
      // Fallback timeout just in case
      if (pendingResponseTimeoutRef.current) {
        clearTimeout(pendingResponseTimeoutRef.current);
      }
      // We don't really have a good fallback if we don't use REST at all.
      // If WS fails, we just don't see the message.
      
    } catch (error) {
      console.error("Failed to send message:", error);
      // Remove optimistic message on error (only if it was added)
      if (userMsg) {
        setMessages((prev) => prev.filter((m) => m.id !== userMsg!.id));
      }
      const errorMsg: Message = {
        id: (Date.now() + 1).toString(),
        text: "Failed to send message. Please try again later.",
        sender: "bot",
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
    <>
      {isOpen && (
        <ChatWindowContainer elevation={3}>
          <ChatHeader>
            <Box sx={{ display: "flex", alignItems: "center", gap: "8px" }}>
              <BotIcon sx={{ color: "#00ffff" }} />
              <ChatTitle>NEXUS SUPPORT</ChatTitle>
            </Box>
            <ChatCloseButton onClick={() => setIsOpen(false)}>
              <CloseIcon />
            </ChatCloseButton>
          </ChatHeader>

          <ChatMessagesArea>
            {messages.map((msg) => (
              <ChatMessageBubble key={msg.id} isUser={msg.sender === "user"}>
                {msg.text}
              </ChatMessageBubble>
            ))}
            <div ref={messagesEndRef} />
          </ChatMessagesArea>

          {isLiveChat ? (
            <Box sx={{ display: 'flex', flexDirection: 'column' }}>
              <Box sx={{ px: 2, pb: 1, display: 'flex', justifyContent: 'flex-end' }}>
                <Button 
                  variant="text" 
                  size="small" 
                  sx={{ color: '#ff00ff', fontSize: '10px' }}
                  onClick={() => handleSendMessage("I need to speak with a human admin")}
                >
                  Request Admin
                </Button>
              </Box>
              <ChatInputContainer>
                <ChatInput
                  placeholder="Type your message..."
                  variant="outlined"
                  size="small"
                  value={inputValue}
                  onChange={(e) => setInputValue(e.target.value)}
                  onKeyDown={handleKeyPress}
                  disabled={isSending}
                />
                <SendButton 
                  onClick={() => handleSendMessage()}
                  disabled={!inputValue.trim() || isSending}
                >
                  <SendIcon />
                </SendButton>
              </ChatInputContainer>
            </Box>
          ) : (
            <ChatOptionsContainer>
              {topics.map((topic) => (
                <ChatOptionButton
                  key={topic.id}
                  variant="outlined"
                  fullWidth
                  onClick={() => handleOptionClick(topic.id)}
                >
                  {topic.label}
                </ChatOptionButton>
              ))}
            </ChatOptionsContainer>
          )}
        </ChatWindowContainer>
      )}

      <ChatFloatingButton onClick={() => setIsOpen(!isOpen)}>
        {isOpen ? <CloseIcon /> : <ChatIcon />}
      </ChatFloatingButton>
    </>
  );
}
