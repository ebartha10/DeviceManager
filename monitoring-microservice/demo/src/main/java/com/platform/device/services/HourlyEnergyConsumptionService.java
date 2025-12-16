package com.platform.device.services;

import com.platform.device.config.RabbitMQConfig;
import com.platform.device.dtos.DailyEnergyConsumptionDTO;
import com.platform.device.dtos.HourlyConsumptionDTO;
import com.platform.device.dtos.RealtimeConsumptionDTO;
import com.platform.device.entities.Device;
import com.platform.device.entities.HourlyEnergyConsumption;
import com.platform.device.messaging.OverconsumptionNotificationMessage;
import com.platform.device.repositories.DeviceRepository;
import com.platform.device.repositories.HourlyEnergyConsumptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HourlyEnergyConsumptionService {
        private static final Logger LOGGER = LoggerFactory.getLogger(HourlyEnergyConsumptionService.class);

        @Autowired
        private HourlyEnergyConsumptionRepository hourlyEnergyConsumptionRepository;

        @Autowired
        private DeviceRepository deviceRepository;

        @Autowired(required = false)
        private SimpMessagingTemplate messagingTemplate;

        @Autowired
        private RabbitTemplate rabbitTemplate;

        @Value("${energy.consumption.threshold:100.0}")
        private Double consumptionThreshold;

        public void processMeasurement(UUID deviceId, LocalDateTime timestamp, Double measurementValue) {
                LocalDateTime hourTimestamp = timestamp.withMinute(0).withSecond(0).withNano(0);

                Optional<HourlyEnergyConsumption> existingConsumption = hourlyEnergyConsumptionRepository
                                .findByDeviceIdAndHourTimestamp(deviceId, hourTimestamp);

                Double hourlyTotal;
                if (existingConsumption.isPresent()) {
                        HourlyEnergyConsumption consumption = existingConsumption.get();
                        consumption.setTotalEnergyConsumption(
                                        consumption.getTotalEnergyConsumption() + measurementValue);
                        hourlyTotal = consumption.getTotalEnergyConsumption();
                        hourlyEnergyConsumptionRepository.save(consumption);
                        LOGGER.debug("Updated hourly energy consumption for device {} at hour {}: {}",
                                        deviceId, hourTimestamp, hourlyTotal);
                } else {
                        HourlyEnergyConsumption newConsumption = new HourlyEnergyConsumption();
                        newConsumption.setDeviceId(deviceId);
                        newConsumption.setHourTimestamp(hourTimestamp);
                        newConsumption.setTotalEnergyConsumption(measurementValue);
                        hourlyTotal = measurementValue;
                        hourlyEnergyConsumptionRepository.save(newConsumption);
                        LOGGER.debug("Created hourly energy consumption for device {} at hour {}: {}",
                                        deviceId, hourTimestamp, measurementValue);
                }

                publishRealtimeUpdate(deviceId, timestamp, measurementValue, hourTimestamp, hourlyTotal);

                // Check for overconsumption threshold
                checkAndNotifyOverconsumption(deviceId, hourlyTotal, timestamp);
        }

        private void checkAndNotifyOverconsumption(UUID deviceId, Double hourlyConsumption, LocalDateTime timestamp) {
                if (hourlyConsumption != null && hourlyConsumption > consumptionThreshold) {
                        LOGGER.warn("Device {} exceeded consumption threshold: {} > {}", deviceId, hourlyConsumption,
                                        consumptionThreshold);

                        try {
                                // Get device name from local repository
                                String deviceName = "Unknown Device";
                                Optional<Device> device = deviceRepository.findById(deviceId);
                                if (device.isPresent() && device.get().getName() != null) {
                                        deviceName = device.get().getName();
                                }

                                // Create notification for device (not user-specific, since device can have
                                // multiple owners)
                                OverconsumptionNotificationMessage notification = new OverconsumptionNotificationMessage();
                                notification.setDeviceId(deviceId);
                                notification.setUserId(null); // Not user-specific, device can have multiple owners
                                notification.setDeviceName(deviceName);
                                notification.setCurrentConsumption(hourlyConsumption);
                                notification.setThreshold(consumptionThreshold);
                                notification.setTimestamp(timestamp);

                                rabbitTemplate.convertAndSend(
                                                RabbitMQConfig.OVERCONSUMPTION_NOTIFICATIONS_EXCHANGE,
                                                RabbitMQConfig.OVERCONSUMPTION_NOTIFICATION_ROUTING_KEY,
                                                notification);

                                // Also send via WebSocket
                                if (messagingTemplate != null) {
                                        messagingTemplate.convertAndSend(
                                                        "/exchange/amq.topic/notifications.device." + deviceId,
                                                        notification);
                                }

                                LOGGER.info("Published overconsumption notification for device {}", deviceId);
                        } catch (Exception e) {
                                LOGGER.error("Failed to send overconsumption notification for device {}: {}", deviceId,
                                                e.getMessage(),
                                                e);
                        }
                }
        }

        public DailyEnergyConsumptionDTO getDailyConsumption(UUID deviceId, LocalDate date) {
                LocalDateTime startOfDay = date.atStartOfDay();
                LocalDateTime endOfDay = date.atTime(23, 59, 59);

                List<HourlyEnergyConsumption> consumptions = hourlyEnergyConsumptionRepository
                                .findByDeviceIdAndHourTimestampBetween(
                                                deviceId, startOfDay, endOfDay);

                List<HourlyConsumptionDTO> hourlyConsumptions = consumptions.stream()
                                .map(consumption -> new HourlyConsumptionDTO(
                                                consumption.getHourTimestamp(),
                                                consumption.getTotalEnergyConsumption()))
                                .sorted((a, b) -> a.getHourTimestamp().compareTo(b.getHourTimestamp()))
                                .collect(Collectors.toList());

                Double totalConsumption = hourlyConsumptions.stream()
                                .mapToDouble(HourlyConsumptionDTO::getConsumption)
                                .sum();

                return new DailyEnergyConsumptionDTO(
                                deviceId,
                                date,
                                totalConsumption,
                                hourlyConsumptions);
        }

        private void publishRealtimeUpdate(UUID deviceId, LocalDateTime timestamp,
                        Double measurementValue, LocalDateTime hourTimestamp,
                        Double hourlyTotal) {
                if (messagingTemplate != null) {
                        RealtimeConsumptionDTO update = new RealtimeConsumptionDTO(
                                        deviceId,
                                        timestamp,
                                        measurementValue,
                                        hourTimestamp,
                                        hourlyTotal);

                        // Send to device-specific topic
                        messagingTemplate.convertAndSend("/exchange/amq.topic/consumption." + deviceId, update);

                        LOGGER.debug("Published real-time consumption update for device {}", deviceId);
                }
        }
}
