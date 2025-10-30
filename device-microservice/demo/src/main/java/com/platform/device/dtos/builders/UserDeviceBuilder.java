package com.platform.device.dtos.builders;

import com.platform.device.dtos.UserDeviceDTO;
import com.platform.device.entities.Device;
import com.platform.device.entities.UserDevice;
import com.platform.device.entities.primaryKeys.UserDeviceId;

public class UserDeviceBuilder {

    public static UserDeviceDTO fromPersitance(UserDevice givenUserDevice){
        return new UserDeviceDTO(givenUserDevice.getId().getUserId(), givenUserDevice.getId().getDeviceId());
    }

    public static UserDevice toEntity(UserDeviceDTO givenUserDeviceDTO, Device device){
        UserDeviceId userDeviceId = new UserDeviceId(givenUserDeviceDTO.getUserId(), givenUserDeviceDTO.getDeviceId());
        return new UserDevice(userDeviceId, device);
    }
}
