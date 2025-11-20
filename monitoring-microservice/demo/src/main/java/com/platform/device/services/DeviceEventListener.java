package com.platform.device.services;

import com.platform.device.config.RabbitMQConfig;
import com.platform.device.dtos.DeviceDTO;
import com.platform.device.entities.Device;
import com.platform.device.messaging.DeviceEventMessage;
import com.platform.device.repositories.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DeviceEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceEventListener.class);

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceRepository deviceRepository;

    @RabbitListener(queues = RabbitMQConfig.MONITORING_DEVICE_QUEUE)
    public void handleDeviceEvent(DeviceEventMessage message) {
        if ("CREATE".equals(message.getEventType())) {
            UUID deviceId = message.getDeviceId();
            LOGGER.info("Received device creation event for device {} from device microservice", deviceId);

            if (deviceId == null) {
                LOGGER.error("Device ID is null in message, skipping");
                return;
            }

            Optional<Device> existingDevice = deviceRepository.findById(deviceId);
            if (existingDevice.isPresent()) {
                LOGGER.info("Device {} already exists in monitoring microservice, skipping duplicate message", deviceId);
                return;
            }

            try {
                DeviceDTO deviceDTO = new DeviceDTO(deviceId, message.getName(), message.getType());
                deviceService.createDevice(deviceDTO);
                LOGGER.info("Successfully created device {} in monitoring microservice", deviceId);
            } catch (Exception e) {
                LOGGER.error("Failed to create device {} in monitoring microservice: {}", deviceId, e.getMessage(), e);
            }

        } else if ("DELETE".equals(message.getEventType())) {
            UUID deviceId = message.getDeviceId();
            LOGGER.info("Received device deletion event for device {} from device microservice", deviceId);

            Optional<Device> existingDevice = deviceRepository.findById(deviceId);
            if (existingDevice.isEmpty()) {
                LOGGER.info("Device {} does not exist in monitoring microservice, skipping deletion", deviceId);
                return;
            }

            deviceService.deleteById(deviceId);
            LOGGER.info("Successfully deleted device {} from monitoring microservice", deviceId);
        }
    }
}

