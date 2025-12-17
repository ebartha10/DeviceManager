import { Client, type Message } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { tokenStorage } from "./tokenStorage";

const WS_BASE_URL = import.meta.env.VITE_API_BASE_URL || "https://localhost";

export interface ChatWebSocketMessage {
  ticketId: string;
  userId: string;
  sender: "user" | "bot" | "admin";
  text: string;
  timestamp: string;
}

export interface OverconsumptionNotification {
  deviceId: string;
  userId: string | null;
  deviceName: string;
  currentConsumption: number;
  threshold: number;
  timestamp: string;
  message: string;
}

export class ChatWebSocketClient {
  private client: Client | null = null;
  private subscriptions: Map<string, (data: ChatWebSocketMessage) => void> = new Map();

  connect(): Promise<void> {
    return new Promise((resolve, reject) => {
      if (this.client?.connected) {
        resolve();
        return;
      }

      const token = tokenStorage.getToken();
      const wsUrl = `${WS_BASE_URL}/ws/chat`;
      const socket = new SockJS(wsUrl);

      this.client = new Client({
        webSocketFactory: () => socket as any,
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
        connectHeaders: token ? {
          Authorization: `Bearer ${token}`,
        } : {},
        onConnect: () => {
          console.log("Chat WebSocket connected");
          resolve();
        },
        onStompError: (frame) => {
          console.error("Chat WebSocket error:", frame);
          reject(new Error(frame.headers["message"] || "WebSocket connection failed"));
        },
        onDisconnect: () => {
          console.log("Chat WebSocket disconnected");
        },
      });

      this.client.activate();
    });
  }

  subscribeToUserChat(
    userId: string,
    callback: (data: ChatWebSocketMessage) => void
  ): () => void {
    if (!this.client?.connected) {
      console.warn("Chat WebSocket not connected, attempting to connect...");
      this.connect().then(() => {
        this.subscribeToUserChat(userId, callback);
      });
      return () => { };
    }

    const topic = `/topic/chat/${userId}`;
    console.log("[ChatWebSocketClient] Subscribing to topic:", topic);
    const subscription = this.client.subscribe(topic, (message: Message) => {
      try {
        console.log("[ChatWebSocketClient] Raw message received on", topic, ":", message.body);
        const data: ChatWebSocketMessage = JSON.parse(message.body);
        console.log("[ChatWebSocketClient] Parsed message:", data);
        callback(data);
      } catch (error) {
        console.error("Error parsing chat WebSocket message:", error);
      }
    });

    this.subscriptions.set(topic, callback);

    return () => {
      subscription.unsubscribe();
      this.subscriptions.delete(topic);
    };
  }

  subscribeToAdminChat(
    callback: (data: ChatWebSocketMessage) => void
  ): () => void {
    if (!this.client?.connected) {
      console.warn("Chat WebSocket not connected, attempting to connect...");
      this.connect().then(() => {
        this.subscribeToAdminChat(callback);
      });
      return () => { };
    }

    const topic = `/topic/chat/admin`;
    const subscription = this.client.subscribe(topic, (message: Message) => {
      try {
        const data: ChatWebSocketMessage = JSON.parse(message.body);
        callback(data);
      } catch (error) {
        console.error("Error parsing admin chat WebSocket message:", error);
      }
    });

    this.subscriptions.set(topic, callback);

    return () => {
      subscription.unsubscribe();
      this.subscriptions.delete(topic);
    };
  }

  subscribeToDeviceNotifications(
    deviceId: string,
    callback: (data: OverconsumptionNotification) => void
  ): () => void {
    if (!this.client?.connected) {
      console.warn("WebSocket not connected for notifications. Please ensure connect() is called first.");
      return () => { };
    }

    const topic = `/topic/notifications/device/${deviceId}`;
    const subscription = this.client.subscribe(topic, (message: Message) => {
      try {
        const data: OverconsumptionNotification = JSON.parse(message.body);
        callback(data);
      } catch (error) {
        console.error("Error parsing notification WebSocket message:", error);
      }
    });

    this.subscriptions.set(topic, callback);

    return () => {
      subscription.unsubscribe();
      this.subscriptions.delete(topic);
    };
  }

  sendMessage(userId: string, message: string): void {
    if (!this.client?.connected) {
      console.warn("Chat WebSocket not connected, cannot send message");
      return;
    }

    this.client.publish({
      destination: "/app/sendMessage",
      body: JSON.stringify({
        userId,
        message,
        role: "USER"
      }),
    });
  }

  sendAdminMessage(ticketId: string, message: string): void {
    if (!this.client?.connected) {
      console.warn("Chat WebSocket not connected, cannot send admin message");
      return;
    }

    this.client.publish({
      destination: "/app/sendMessage",
      body: JSON.stringify({
        ticketId,
        message,
        role: "ADMIN"
      }),
    });
  }

  disconnect(): void {
    if (this.client) {
      this.client.deactivate();
      this.client = null;
      this.subscriptions.clear();
    }
  }

  isConnected(): boolean {
    return this.client?.connected || false;
  }
}

export const chatWebSocketClient = new ChatWebSocketClient();
