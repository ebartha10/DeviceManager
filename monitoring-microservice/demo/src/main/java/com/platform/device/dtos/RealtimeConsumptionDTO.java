package com.platform.device.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RealtimeConsumptionDTO {
    private UUID deviceId;
    private LocalDateTime timestamp;
    private Double consumption;
    private LocalDateTime hourTimestamp;
    private Double hourlyTotal;
}

