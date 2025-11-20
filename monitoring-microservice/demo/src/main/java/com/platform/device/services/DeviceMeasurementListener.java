package com.platform.device.services;

import com.platform.device.config.RabbitMQConfig;
import com.platform.device.messaging.DeviceMeasurementMessage;
import com.platform.device.repositories.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeviceMeasurementListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceMeasurementListener.class);

    @Autowired
    private HourlyEnergyConsumptionService hourlyEnergyConsumptionService;

    @Autowired
    private DeviceRepository deviceRepository;

    @RabbitListener(queues = RabbitMQConfig.MONITORING_MEASUREMENTS_QUEUE)
    public void handleDeviceMeasurement(DeviceMeasurementMessage message) {
        UUID deviceId = message.getDeviceId();
        
        if (!deviceRepository.existsById(deviceId)) {
            LOGGER.warn("Received measurement for unknown device {}, skipping", deviceId);
            return;
        }

        LOGGER.info("Received device measurement: device={}, timestamp={}, value={}",
            deviceId, message.getTimestamp(), message.getMeasurementValue());

        hourlyEnergyConsumptionService.processMeasurement(
            deviceId,
            message.getTimestamp(),
            message.getMeasurementValue()
        );
    }
}

