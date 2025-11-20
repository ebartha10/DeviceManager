package com.platform.device.dtos.builders;

import com.platform.device.dtos.DeviceDTO;
import com.platform.device.entities.Device;

public class DeviceBuilder {

    public static DeviceDTO fromPersistance(Device device) {
        return new DeviceDTO(device.getId(), device.getName(), device.getType());
    }

    public static Device toDeviceEntity(DeviceDTO deviceDTO) {
        return new Device(deviceDTO.getId(), deviceDTO.getName(), deviceDTO.getType());
    }
}
