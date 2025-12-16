package com.platform.device.services;

import com.platform.device.config.RabbitMQConfig;
import com.platform.device.dtos.OverconsumptionNotificationDTO;
import com.platform.device.messaging.OverconsumptionNotificationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class OverconsumptionNotificationListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(OverconsumptionNotificationListener.class);

    private final SimpMessagingTemplate messagingTemplate;

    public OverconsumptionNotificationListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.WEBSOCKET_OVERCONSUMPTION_QUEUE)
    public void handleOverconsumptionNotification(OverconsumptionNotificationMessage notification) {
        LOGGER.info("Received overconsumption notification for device {}", notification.getDeviceId());

        // Convert to DTO format for WebSocket
        OverconsumptionNotificationDTO dto = new OverconsumptionNotificationDTO();
        dto.setDeviceId(notification.getDeviceId());
        dto.setUserId(notification.getUserId()); // May be null if device has multiple owners
        dto.setDeviceName(notification.getDeviceName());
        dto.setCurrentConsumption(notification.getCurrentConsumption());
        dto.setThreshold(notification.getThreshold());
        dto.setTimestamp(notification.getTimestamp());
        dto.setMessage(String.format(
            "Device '%s' has exceeded the energy consumption threshold! Current: %.2f kWh/h, Threshold: %.2f kWh/h",
            notification.getDeviceName(),
            notification.getCurrentConsumption(),
            notification.getThreshold()
        ));

        // Send to device-specific topic: /topic/notifications/device/{deviceId}
        // All owners of this device will receive the notification
        String deviceTopic = "/topic/notifications/device/" + notification.getDeviceId();
        messagingTemplate.convertAndSend(deviceTopic, dto);
        LOGGER.info("Sent overconsumption notification to WebSocket topic: {}", deviceTopic);
    }
}
