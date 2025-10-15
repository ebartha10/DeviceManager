package com.platform.device.services;

import com.platform.device.dtos.DeviceDTO;
import com.platform.device.dtos.UserDeviceDTO;
import com.platform.device.dtos.builders.DeviceBuilder;
import com.platform.device.dtos.builders.UserDeviceBuilder;
import com.platform.device.entities.Device;
import com.platform.device.entities.UserDevice;
import com.platform.device.entities.primaryKeys.UserDeviceId;
import com.platform.device.handlers.exceptions.model.ResourceNotFoundException;
import com.platform.device.repositories.DeviceRepository;
import com.platform.device.repositories.UserDeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserDeviceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDeviceRepository userDeviceRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    public List<DeviceDTO> getDevicesForUser(UUID givenUserId) {
        return this.userDeviceRepository.findByIdUserId(givenUserId)
                .stream()
                .map(userDevice -> DeviceBuilder.fromPersistance(userDevice.getDevice()))
                .toList();
    }

    public UserDeviceDTO createUserDevice(UserDeviceDTO userDeviceDTO) {
        Optional<Device> device = this.deviceRepository.findById(userDeviceDTO.getDevice().getId());
        if(device.isEmpty()) {
            LOGGER.error("Device with id {} was not found in db", userDeviceDTO.getUserId());
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + userDeviceDTO.getUserId());
        }

        UserDevice newUserDevice = UserDeviceBuilder.toEntity(userDeviceDTO);

        newUserDevice = this.userDeviceRepository.save(newUserDevice);
        LOGGER.debug("UserDevice with userId {} and deviceId {} was inserted in db", newUserDevice.getId().getUserId(), newUserDevice.getId().getDeviceId());

        return UserDeviceBuilder.fromPersitance(newUserDevice);
    }

    public void deleteUserDevice(UUID givenUserId, UUID givenDeviceId) {
        UserDeviceId userDeviceId = new UserDeviceId(givenUserId, givenDeviceId);

        if(!this.userDeviceRepository.existsById(userDeviceId)) {
            LOGGER.error("UserDevice with userId {} and deviceId {} was not found in db", givenUserId, givenDeviceId);
            throw new ResourceNotFoundException(UserDevice.class.getSimpleName() + " with id: " + userDeviceId);
        }

        this.userDeviceRepository.deleteById(userDeviceId);
        LOGGER.debug("UserDevice with userId {} and deviceId {} was deleted from db", givenUserId, givenDeviceId);
    }
}
