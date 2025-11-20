package com.platform.device.repositories;

import com.platform.device.entities.HourlyEnergyConsumption;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HourlyEnergyConsumptionRepository extends CrudRepository<HourlyEnergyConsumption, UUID> {
    Optional<HourlyEnergyConsumption> findByDeviceIdAndHourTimestamp(UUID deviceId, LocalDateTime hourTimestamp);
}

