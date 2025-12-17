package com.platform.device.messaging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OverconsumptionNotificationMessage {
    private UUID deviceId;
    private UUID userId;
    private String deviceName;
    private Double currentConsumption;
    private Double threshold;
    private LocalDateTime timestamp;
}
