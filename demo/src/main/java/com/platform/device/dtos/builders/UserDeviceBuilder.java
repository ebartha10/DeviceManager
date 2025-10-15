package com.platform.device.dtos.builders;

import com.platform.device.dtos.UserDeviceDTO;
import com.platform.device.entities.UserDevice;
import com.platform.device.entities.primaryKeys.UserDeviceId;

public class UserDeviceBuilder {

    public static UserDeviceDTO fromPersitance(UserDevice givenUserDevice){
        return new UserDeviceDTO(givenUserDevice.getId().getUserId(), givenUserDevice.getDevice(), givenUserDevice.getDeviceName(), givenUserDevice.getSerialNumber());
    }

    public static UserDevice toEntity(UserDeviceDTO givenUserDeviceDTO){
        UserDeviceId userDeviceId = new UserDeviceId(givenUserDeviceDTO.getUserId(), givenUserDeviceDTO.getDevice().getId());
        return new UserDevice(userDeviceId, givenUserDeviceDTO.getDevice(), givenUserDeviceDTO.getDeviceName(), givenUserDeviceDTO.getSerialNumber());
    }
}
