package com.platform.device.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "hourly_energy_consumption")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HourlyEnergyConsumption {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "device_id", nullable = false)
    private UUID deviceId;

    @Column(name = "hour_timestamp", nullable = false)
    private LocalDateTime hourTimestamp;

    @Column(name = "total_energy_consumption", nullable = false)
    private Double totalEnergyConsumption;
}

