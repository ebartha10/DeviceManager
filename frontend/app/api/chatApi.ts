import { apiClient } from "./apiClient";
import { tokenStorage } from "./tokenStorage";

export interface ChatMessage {
  sender: "user" | "bot" | "admin";
  text: string;
  timestamp: string;
}

export interface SupportTopic {
  id: string;
  label: string;
  response: string;
}

export interface SupportTicketMessage {
  sender: string;
  messageContent: string;
  timestamp: string;
}

export interface SupportTicket {
  ticketId: string;
  userId: string;
  createdAt: string;
  status: string;
  messages: SupportTicketMessage[];
}

export const chatApi = {
  sendMessage: async (message: string): Promise<ChatMessage> => {
    const userId = tokenStorage.getUserId();
    return apiClient.post<ChatMessage>("/chat/send", { message }, {
      headers: userId ? { "X-User-Id": userId } : {},
    });
  },
  
  getTopics: async (): Promise<SupportTopic[]> => {
    return apiClient.get<SupportTopic[]>("/chat/topics");
  },
  
  // Admin endpoints
  getPendingTickets: async (): Promise<SupportTicket[]> => {
    return apiClient.get<SupportTicket[]>("/chat/admin/pending");
  },
  
  getActiveTickets: async (): Promise<SupportTicket[]> => {
    return apiClient.get<SupportTicket[]>("/chat/admin/active");
  },
  
  getTicket: async (ticketId: string): Promise<SupportTicket> => {
    return apiClient.get<SupportTicket>(`/chat/admin/ticket/${ticketId}`);
  },
  
  sendAdminMessage: async (ticketId: string, message: string): Promise<ChatMessage> => {
    return apiClient.post<ChatMessage>("/chat/admin/send", { ticketId, message });
  }
};

