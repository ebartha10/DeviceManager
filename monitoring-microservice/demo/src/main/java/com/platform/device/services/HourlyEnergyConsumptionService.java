package com.platform.device.services;

import com.platform.device.entities.HourlyEnergyConsumption;
import com.platform.device.repositories.HourlyEnergyConsumptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class HourlyEnergyConsumptionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HourlyEnergyConsumptionService.class);

    @Autowired
    private HourlyEnergyConsumptionRepository hourlyEnergyConsumptionRepository;

    public void processMeasurement(UUID deviceId, LocalDateTime timestamp, Double measurementValue) {
        LocalDateTime hourTimestamp = timestamp.withMinute(0).withSecond(0).withNano(0);

        Optional<HourlyEnergyConsumption> existingConsumption = 
            hourlyEnergyConsumptionRepository.findByDeviceIdAndHourTimestamp(deviceId, hourTimestamp);

        if (existingConsumption.isPresent()) {
            HourlyEnergyConsumption consumption = existingConsumption.get();
            consumption.setTotalEnergyConsumption(
                consumption.getTotalEnergyConsumption() + measurementValue
            );
            hourlyEnergyConsumptionRepository.save(consumption);
            LOGGER.debug("Updated hourly energy consumption for device {} at hour {}: {}",
                deviceId, hourTimestamp, consumption.getTotalEnergyConsumption());
        } else {
            HourlyEnergyConsumption newConsumption = new HourlyEnergyConsumption();
            newConsumption.setDeviceId(deviceId);
            newConsumption.setHourTimestamp(hourTimestamp);
            newConsumption.setTotalEnergyConsumption(measurementValue);
            hourlyEnergyConsumptionRepository.save(newConsumption);
            LOGGER.debug("Created hourly energy consumption for device {} at hour {}: {}",
                deviceId, hourTimestamp, measurementValue);
        }
    }
}

