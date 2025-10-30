package com.platform.device.controllers;

import com.platform.device.dtos.DeviceDTO;
import com.platform.device.dtos.UserDeviceDTO;
import com.platform.device.services.DeviceService;
import com.platform.device.services.UserDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/device")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @Autowired
    private UserDeviceService userDeviceService;

    @GetMapping("/get-all")
    @ResponseBody
    public ResponseEntity<List<DeviceDTO>> getAllDevices(){
        return ResponseEntity.ok(this.deviceService.getAllDevices());
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<DeviceDTO> getDeviceById(@RequestParam UUID id){
        DeviceDTO device = this.deviceService.getDeviceById(id);
        return ResponseEntity.ok(device);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<DeviceDTO> createDevice(@RequestBody DeviceDTO device){
        DeviceDTO createdDevice = this.deviceService.createDevice(device);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("?{id}")
                .buildAndExpand(createdDevice.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping
    @ResponseBody
    public ResponseEntity<DeviceDTO> updateDevice(@RequestBody DeviceDTO device){
        DeviceDTO newDevice = this.deviceService.updateUser(device);
        return ResponseEntity.accepted().body(newDevice);
    }

    @DeleteMapping
    @ResponseBody
    public ResponseEntity<String> deleteDeviceById(@RequestParam UUID id){
        this.deviceService.deleteById(id);
        return ResponseEntity.ok("Deletion Successful");
    }

    @PostMapping("user-device")
    @ResponseBody
    // DTO only contains userId and deviceId
    public UserDeviceDTO addDeviceForUser(@RequestBody UserDeviceDTO userDeviceDTO){
        return this.userDeviceService.createUserDevice(userDeviceDTO);
    }

    @DeleteMapping("user-device")
    @ResponseBody
    public ResponseEntity<String> deleteDeviceForUser(@RequestParam UUID userId, @RequestParam UUID deviceId){
        this.userDeviceService.deleteUserDevice(userId, deviceId);
        return ResponseEntity.ok("Deletion Successful");
    }

    // GET DEVICE FOR USER
    @GetMapping("user-device")
    @ResponseBody
    public ResponseEntity<List<DeviceDTO>> getDevicesForUser(@RequestParam UUID userId){
        List<DeviceDTO> devices = this.userDeviceService.getDevicesForUser(userId);
        return ResponseEntity.ok(devices);
    }
}
