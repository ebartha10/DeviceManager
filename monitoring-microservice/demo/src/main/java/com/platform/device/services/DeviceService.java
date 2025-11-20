package com.platform.device.services;

import com.platform.device.dtos.DeviceDTO;
import com.platform.device.dtos.builders.DeviceBuilder;
import com.platform.device.entities.Device;
import com.platform.device.handlers.exceptions.model.ResourceNotFoundException;
import com.platform.device.repositories.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeviceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);

    @Autowired
    private DeviceRepository deviceRepository;

    public List<DeviceDTO> getAllDevices() {
        List<Device> users = (List<Device>) this.deviceRepository.findAll();
        return users.stream().map(DeviceBuilder::fromPersistance).toList();
    }

    public DeviceDTO getDeviceById(UUID id) {
        Optional<Device> device = this.deviceRepository.findById(id);
        if(device.isEmpty()) {
            LOGGER.error("Device with id {} was not found in db", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }

        return DeviceBuilder.fromPersistance(device.get());
    }

    public DeviceDTO createDevice(DeviceDTO device) {
        if (device.getId() == null) {
            LOGGER.error("Cannot create device with null ID");
            throw new IllegalArgumentException("Device ID cannot be null");
        }

        Device newDevice = DeviceBuilder.toDeviceEntity(device);
        LOGGER.debug("Creating device with id: {}, name: {}, type: {}", newDevice.getId(), newDevice.getName(), newDevice.getType());

        try {
            newDevice = this.deviceRepository.save(newDevice);
            LOGGER.info("Device with id {} was successfully inserted in db", newDevice.getId());
        } catch (Exception e) {
            LOGGER.error("Failed to save device with id {}: {}", device.getId(), e.getMessage(), e);
            throw e;
        }

        return DeviceBuilder.fromPersistance(newDevice);
    }

    public DeviceDTO updateDevice(DeviceDTO givenDevice) {
        Optional<Device> device = this.deviceRepository.findById(givenDevice.getId());
        if(device.isEmpty()) {
            LOGGER.error("Device with id {} was not found in db", givenDevice.getId());
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + givenDevice.getId());
        }

        Device existingDevice = device.get();
        existingDevice.setName(givenDevice.getName());
        existingDevice.setType(givenDevice.getType());

        existingDevice = deviceRepository.save(existingDevice);
        LOGGER.debug("Device with id {} was updated in db", existingDevice.getId());

        return DeviceBuilder.fromPersistance(existingDevice);
    }

    public void deleteById(UUID id) {
        Optional<Device> device = this.deviceRepository.findById(id);
        if(device.isEmpty()) {
            LOGGER.error("Device with id {} was not found in db", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }

        this.deviceRepository.deleteById(id);
        LOGGER.debug("Device with id {} was deleted from db", id);
    }

}
