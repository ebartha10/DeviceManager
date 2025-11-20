package com.platform.device.services;

import com.platform.device.config.RabbitMQConfig;
import com.platform.device.messaging.DeviceEventMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeviceEventPublisher {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceEventPublisher.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publishDeviceCreated(UUID deviceId, String name, String type) {
        DeviceEventMessage message = new DeviceEventMessage("CREATE", deviceId, name, type);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.DEVICE_EVENTS_EXCHANGE,
                RabbitMQConfig.DEVICE_CREATE_ROUTING_KEY,
                message
        );
        LOGGER.info("Published device creation event for device {} to device events exchange", deviceId);
    }

    public void publishDeviceDeleted(UUID deviceId) {
        DeviceEventMessage message = new DeviceEventMessage("DELETE", deviceId, null, null);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.DEVICE_EVENTS_EXCHANGE,
                RabbitMQConfig.DEVICE_DELETE_ROUTING_KEY,
                message
        );
        LOGGER.info("Published device deletion event for device {} to device events exchange", deviceId);
    }
}

