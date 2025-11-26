import { Client, type Message } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import type { RealtimeConsumption } from "./monitoringApi";
import { tokenStorage } from "./tokenStorage";

const WS_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost";

export class WebSocketClient {
  private client: Client | null = null;
  private subscriptions: Map<string, (data: RealtimeConsumption) => void> = new Map();

  connect(): Promise<void> {
    return new Promise((resolve, reject) => {
      if (this.client?.connected) {
        resolve();
        return;
      }

      const token = tokenStorage.getToken();
      const wsUrl = token 
        ? `${WS_BASE_URL}/monitoring/ws/consumption?token=${encodeURIComponent(token)}`
        : `${WS_BASE_URL}/monitoring/ws/consumption`;
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
          console.log("WebSocket connected");
          resolve();
        },
        onStompError: (frame) => {
          console.error("WebSocket error:", frame);
          reject(new Error(frame.headers["message"] || "WebSocket connection failed"));
        },
        onDisconnect: () => {
          console.log("WebSocket disconnected");
        },
      });

      this.client.activate();
    });
  }

  subscribeToDevice(
    deviceId: string,
    callback: (data: RealtimeConsumption) => void
  ): () => void {
    if (!this.client?.connected) {
      console.warn("WebSocket not connected, attempting to connect...");
      this.connect().then(() => {
        this.subscribeToDevice(deviceId, callback);
      });
      return () => {};
    }

    const topic = `/topic/consumption/${deviceId}`;
    const subscription = this.client.subscribe(topic, (message: Message) => {
      try {
        const data: RealtimeConsumption = JSON.parse(message.body);
        callback(data);
      } catch (error) {
        console.error("Error parsing WebSocket message:", error);
      }
    });

    this.subscriptions.set(topic, callback);

    return () => {
      subscription.unsubscribe();
      this.subscriptions.delete(topic);
    };
  }

  subscribeToAll(callback: (data: RealtimeConsumption) => void): () => void {
    if (!this.client?.connected) {
      console.warn("WebSocket not connected, attempting to connect...");
      this.connect().then(() => {
        this.subscribeToAll(callback);
      });
      return () => {};
    }

    const topic = `/topic/consumption/all`;
    const subscription = this.client.subscribe(topic, (message: Message) => {
      try {
        const data: RealtimeConsumption = JSON.parse(message.body);
        callback(data);
      } catch (error) {
        console.error("Error parsing WebSocket message:", error);
      }
    });

    this.subscriptions.set(topic, callback);

    return () => {
      subscription.unsubscribe();
      this.subscriptions.delete(topic);
    };
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

export const websocketClient = new WebSocketClient();

