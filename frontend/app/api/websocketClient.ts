import { Client, type Message } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import type { RealtimeConsumption } from "./monitoringApi";
import { tokenStorage } from "./tokenStorage";

const WS_BASE_URL = import.meta.env.VITE_API_BASE_URL || "https://localhost";

export interface OverconsumptionNotification {
  deviceId: string;
  userId: string | null;
  deviceName: string;
  currentConsumption: number;
  threshold: number;
  timestamp: string;
  message: string;
}

export class MonitoringWebSocketClient {
  private client: Client | null = null;
  private subscriptions: Map<string, (data: any) => void> = new Map();
  private pendingSubscriptions: Array<{ topic: string, callback: (data: any) => void }> = [];

  connect(): Promise<void> {
    if (this.client?.connected) {
      return Promise.resolve();
    }

    // Check if a connection is already in progress
    if (this.client && !this.client.connected && this.client.active) {
      return new Promise((resolve) => {
        const checkConnection = setInterval(() => {
          if (this.client?.connected) {
            clearInterval(checkConnection);
            resolve();
          }
        }, 100);
      });
    }

    return new Promise((resolve, reject) => {
      const token = tokenStorage.getToken();
      const wsUrl = token
        ? `${WS_BASE_URL}/monitoring/ws/consumption?token=${encodeURIComponent(token)}`
        : `${WS_BASE_URL}/monitoring/ws/consumption`;

      console.log("Connecting to Monitoring WebSocket at:", wsUrl);

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
          console.log("Monitoring WebSocket connected");
          this.resubscribePending();
          resolve();
        },
        onStompError: (frame) => {
          console.error("Monitoring WebSocket error:", frame);
          // Only reject if this is the initial connection attempt
          // If auto-reconnecting, we handle it internally
        },
        onDisconnect: () => {
          console.log("Monitoring WebSocket disconnected");
        },
      });

      this.client.activate();
    });
  }

  private resubscribePending() {
    // Re-subscribe to all active subscriptions on reconnect
    this.subscriptions.forEach((callback, topic) => {
      this.doSubscribe(topic, callback);
    });
  }

  private doSubscribe(topic: string, callback: (data: any) => void) {
    if (!this.client?.connected) return;

    this.client.subscribe(topic, (message: Message) => {
      try {
        const data = JSON.parse(message.body);
        callback(data);
      } catch (error) {
        console.error(`Error parsing message for topic ${topic}:`, error);
      }
    });
  }

  subscribeToDevice(
    deviceId: string,
    callback: (data: RealtimeConsumption) => void
  ): () => void {
    const topic = `/exchange/amq.topic/consumption.${deviceId}`;
    this.subscriptions.set(topic, callback);

    if (this.client?.connected) {
      this.doSubscribe(topic, callback);
    } else {
      this.connect();
    }

    return () => {
      // We don't strictly unsubscribe from STOMP to keep it simple with shared connections,
      // but we remove from our tracking map
      this.subscriptions.delete(topic);
    };
  }

  subscribeToNotifications(
    deviceId: string,
    callback: (data: OverconsumptionNotification) => void
  ): () => void {
    const topic = `/exchange/amq.topic/notifications.device.${deviceId}`;
    this.subscriptions.set(topic, callback);

    if (this.client?.connected) {
      this.doSubscribe(topic, callback);
    } else {
      this.connect();
    }

    return () => {
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

export const monitoringWebSocketClient = new MonitoringWebSocketClient();
// Export as websocketClient for backward compatibility
export const websocketClient = monitoringWebSocketClient;

