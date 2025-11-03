package com.platform.device.controllers;

import com.platform.device.dtos.DeviceDTO;
import com.platform.device.dtos.UserDeviceDTO;
import com.platform.device.entities.Role;
import com.platform.device.services.DeviceService;
import com.platform.device.services.SecurityService;
import com.platform.device.services.UserDeviceService;
import jakarta.servlet.http.HttpServletRequest;
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
    
    @Autowired
    private SecurityService securityService;

    @GetMapping("/get-all")
    @ResponseBody
    public ResponseEntity<List<DeviceDTO>> getAllDevices(HttpServletRequest request){
        // USER and ADMIN can access
        securityService.requireRole(request, Role.USER, Role.ADMIN);
        return ResponseEntity.ok(this.deviceService.getAllDevices());
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<DeviceDTO> getDeviceById(@RequestParam UUID id, HttpServletRequest request){
        // USER and ADMIN can access
        securityService.requireRole(request, Role.USER, Role.ADMIN);
        DeviceDTO device = this.deviceService.getDeviceById(id);
        return ResponseEntity.ok(device);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<DeviceDTO> createDevice(@RequestBody DeviceDTO device, HttpServletRequest request){
        // ADMIN only
        securityService.requireAdmin(request);
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
    public ResponseEntity<DeviceDTO> updateDevice(@RequestBody DeviceDTO device, HttpServletRequest request){
        // ADMIN only
        securityService.requireAdmin(request);
        DeviceDTO newDevice = this.deviceService.updateDevice(device);
        return ResponseEntity.accepted().body(newDevice);
    }

    @DeleteMapping
    @ResponseBody
    public ResponseEntity<String> deleteDeviceById(@RequestParam UUID id, HttpServletRequest request){
        // ADMIN only
        securityService.requireAdmin(request);
        this.deviceService.deleteById(id);
        return ResponseEntity.ok("Deletion Successful");
    }

    @PostMapping("user-device")
    @ResponseBody
    // DTO only contains userId and deviceId
    public UserDeviceDTO addDeviceForUser(@RequestBody UserDeviceDTO userDeviceDTO, HttpServletRequest request){
        // USER and ADMIN can access
        securityService.requireRole(request, Role.USER, Role.ADMIN);
        if(securityService.getUserRole(request) == Role.USER) {
            securityService.requireUser(request, userDeviceDTO.getUserId());
        }
        return this.userDeviceService.createUserDevice(userDeviceDTO);
    }

    @DeleteMapping("user-device")
    @ResponseBody
    public ResponseEntity<String> deleteDeviceForUser(@RequestParam UUID userId, @RequestParam UUID deviceId, HttpServletRequest request) {
        // USER and ADMIN can access
        securityService.requireRole(request, Role.USER, Role.ADMIN);
        if (securityService.getUserRole(request) == Role.USER){
            securityService.requireUser(request, userId);

        }
        this.userDeviceService.deleteUserDevice(userId, deviceId);
        return ResponseEntity.ok("Deletion Successful");
    }

    // GET DEVICE FOR USER
    @GetMapping("user-device")
    @ResponseBody
    public ResponseEntity<List<DeviceDTO>> getDevicesForUser(@RequestParam UUID userId, HttpServletRequest request){
        // USER and ADMIN can access
        securityService.requireRole(request, Role.USER, Role.ADMIN);
        securityService.requireUser(request, userId);
        List<DeviceDTO> devices = this.userDeviceService.getDevicesForUser(userId);
        return ResponseEntity.ok(devices);
    }
}
