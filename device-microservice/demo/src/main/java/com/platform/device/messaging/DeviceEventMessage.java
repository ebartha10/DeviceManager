package com.platform.device.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceEventMessage {
    private String eventType;
    private UUID deviceId;
    private String name;
    private String type;
}

