package com.platform.device.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceMeasurementMessage {
    private LocalDateTime timestamp;
    private UUID deviceId;
    private Double measurementValue;
}

