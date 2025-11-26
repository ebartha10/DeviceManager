package com.platform.device.services;

import com.platform.device.dtos.DailyEnergyConsumptionDTO;
import com.platform.device.dtos.HourlyConsumptionDTO;
import com.platform.device.dtos.RealtimeConsumptionDTO;
import com.platform.device.entities.HourlyEnergyConsumption;
import com.platform.device.repositories.HourlyEnergyConsumptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired(required = false)
    private SimpMessagingTemplate messagingTemplate;

    public void processMeasurement(UUID deviceId, LocalDateTime timestamp, Double measurementValue) {
        LocalDateTime hourTimestamp = timestamp.withMinute(0).withSecond(0).withNano(0);

        Optional<HourlyEnergyConsumption> existingConsumption = 
            hourlyEnergyConsumptionRepository.findByDeviceIdAndHourTimestamp(deviceId, hourTimestamp);

        Double hourlyTotal;
        if (existingConsumption.isPresent()) {
            HourlyEnergyConsumption consumption = existingConsumption.get();
            consumption.setTotalEnergyConsumption(
                consumption.getTotalEnergyConsumption() + measurementValue
            );
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
    }

    public DailyEnergyConsumptionDTO getDailyConsumption(UUID deviceId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        List<HourlyEnergyConsumption> consumptions = 
            hourlyEnergyConsumptionRepository.findByDeviceIdAndHourTimestampBetween(
                deviceId, startOfDay, endOfDay
            );

        List<HourlyConsumptionDTO> hourlyConsumptions = consumptions.stream()
            .map(consumption -> new HourlyConsumptionDTO(
                consumption.getHourTimestamp(),
                consumption.getTotalEnergyConsumption()
            ))
            .sorted((a, b) -> a.getHourTimestamp().compareTo(b.getHourTimestamp()))
            .collect(Collectors.toList());

        Double totalConsumption = hourlyConsumptions.stream()
            .mapToDouble(HourlyConsumptionDTO::getConsumption)
            .sum();

        return new DailyEnergyConsumptionDTO(
            deviceId,
            date,
            totalConsumption,
            hourlyConsumptions
        );
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
                hourlyTotal
            );
            
            // Send to device-specific topic
            messagingTemplate.convertAndSend("/topic/consumption/" + deviceId, update);

            // Also send to general consumption topic for all devices
            messagingTemplate.convertAndSend("/topic/consumption/all", update);
            
            LOGGER.debug("Published real-time consumption update for device {}", deviceId);
        }
    }
}

