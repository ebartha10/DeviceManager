import { useState, useEffect, useRef } from "react";
import { Close as CloseIcon, Warning as WarningIcon } from "@mui/icons-material";
import {
  NotificationContainer,
  NotificationCard,
  NotificationHeader,
  NotificationTitle,
  NotificationCloseButton,
  NotificationMessage,
  NotificationDetails,
  NotificationDetailRow,
  NotificationDetailLabel,
  NotificationDetailValue,
} from "./StyledComponents";
import { chatWebSocketClient, type OverconsumptionNotification } from "../../api/chatWebSocketClient";
import type { Device } from "../../api/deviceApi";

interface OverconsumptionNotificationProps {
  userDevices: Device[];
}

interface NotificationWithId extends OverconsumptionNotification {
  id: string;
}

export function OverconsumptionNotification({ userDevices }: OverconsumptionNotificationProps) {
  const [notifications, setNotifications] = useState<NotificationWithId[]>([]);
  const unsubscribeRefs = useRef<Map<string, () => void>>(new Map());
  const autoDismissTimers = useRef<Map<string, NodeJS.Timeout>>(new Map());

  useEffect(() => {
    if (userDevices.length === 0) {
      return;
    }

    // Connect to WebSocket
    chatWebSocketClient
      .connect()
      .then(() => {
        // Subscribe to new devices
        userDevices.forEach((device) => {
          if (!unsubscribeRefs.current.has(device.id)) {
            const unsubscribe = chatWebSocketClient.subscribeToDeviceNotifications(
              device.id,
              (notification) => {
                setNotifications((prev) => {
                  const notificationId = `${notification.deviceId}-${notification.timestamp}`;
                  const exists = prev.some((n) => n.id === notificationId);
                  if (exists) return prev;

                  const notificationWithId: NotificationWithId = { ...notification, id: notificationId };
                  const newNotifications = [notificationWithId, ...prev];

                  const timerId = setTimeout(() => {
                    setNotifications((current) => current.filter((n) => n.id !== notificationId));
                    autoDismissTimers.current.delete(notificationId);
                  }, 10000);

                  autoDismissTimers.current.set(notificationId, timerId);
                  return newNotifications;
                });
              }
            );
            unsubscribeRefs.current.set(device.id, unsubscribe);
          }
        });
      })
      .catch((error) => {
        console.error("Failed to connect notification WebSocket:", error);
      });

    return () => {
      unsubscribeRefs.current.forEach((unsubscribe) => unsubscribe());
      unsubscribeRefs.current.clear();
    };
  }, [userDevices]);

  const handleRemoveNotification = (notificationId: string) => {
    // Clear auto-dismiss timer if exists
    const timer = autoDismissTimers.current.get(notificationId);
    if (timer) {
      clearTimeout(timer);
      autoDismissTimers.current.delete(notificationId);
    }

    setNotifications((prev) => prev.filter((n) => n.id !== notificationId));
  };

  useEffect(() => {
    // Cleanup timers on unmount
    return () => {
      autoDismissTimers.current.forEach((timer) => clearTimeout(timer));
      autoDismissTimers.current.clear();
    };
  }, []);

  if (notifications.length === 0) {
    return null;
  }

  return (
    <NotificationContainer>
      {notifications.slice(0, 5).map((notification) => (
        <NotificationCard key={notification.id}>
          <NotificationHeader>
            <NotificationTitle>
              <WarningIcon sx={{ fontSize: "20px" }} />
              Overconsumption Alert
            </NotificationTitle>
            <NotificationCloseButton
              size="small"
              onClick={() => handleRemoveNotification(notification.id)}
            >
              <CloseIcon fontSize="small" />
            </NotificationCloseButton>
          </NotificationHeader>
          <NotificationMessage>{notification.message}</NotificationMessage>
          <NotificationDetails>
            <NotificationDetailRow>
              <NotificationDetailLabel>Device:</NotificationDetailLabel>
              <NotificationDetailValue>{notification.deviceName}</NotificationDetailValue>
            </NotificationDetailRow>
            <NotificationDetailRow>
              <NotificationDetailLabel>Current Consumption:</NotificationDetailLabel>
              <NotificationDetailValue>
                {notification.currentConsumption.toFixed(2)} kWh/h
              </NotificationDetailValue>
            </NotificationDetailRow>
            <NotificationDetailRow>
              <NotificationDetailLabel>Threshold:</NotificationDetailLabel>
              <NotificationDetailValue>
                {notification.threshold.toFixed(2)} kWh/h
              </NotificationDetailValue>
            </NotificationDetailRow>
          </NotificationDetails>
        </NotificationCard>
      ))}
    </NotificationContainer>
  );
}

