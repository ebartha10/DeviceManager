package com.platform.device.controllers;

import com.platform.device.dtos.DailyEnergyConsumptionDTO;
import com.platform.device.entities.Role;
import com.platform.device.services.HourlyEnergyConsumptionService;
import com.platform.device.services.SecurityService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.UUID;

@Controller
@RequestMapping("/energy-consumption")
public class EnergyConsumptionController {

    @Autowired
    private HourlyEnergyConsumptionService hourlyEnergyConsumptionService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/daily")
    @ResponseBody
    public ResponseEntity<?> getDailyConsumption(
            @RequestParam UUID deviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            HttpServletRequest request) {

        System.out.println("Received daily consumption request for device: " + deviceId + ", date: " + date);

        // USER and ADMIN can access
        securityService.requireRole(request, Role.USER, Role.ADMIN);

        try {
            DailyEnergyConsumptionDTO consumption = hourlyEnergyConsumptionService.getDailyConsumption(deviceId, date);
            System.out.println("DTO Created: " + consumption);

            // Manual serialization to catch errors
            String json = objectMapper.writeValueAsString(consumption);

            return ResponseEntity.ok()
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(json);

        } catch (Exception e) {
            System.err.println("Error processing/serializing daily consumption request: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
