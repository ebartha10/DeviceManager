package com.platform.device.dtos;

import com.platform.device.entities.Device;
import com.platform.device.entities.primaryKeys.UserDeviceId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDeviceDTO {

    private UUID userId;

    private Device device;

    private String deviceName;

    private String serialNumber;
}
