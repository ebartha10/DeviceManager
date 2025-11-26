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

import java.time.LocalDate;
import java.util.UUID;

@Controller
@RequestMapping("/energy-consumption")
public class EnergyConsumptionController {

    @Autowired
    private HourlyEnergyConsumptionService hourlyEnergyConsumptionService;

    @Autowired
    private SecurityService securityService;

    @GetMapping("/daily")
    @ResponseBody
    public ResponseEntity<DailyEnergyConsumptionDTO> getDailyConsumption(
            @RequestParam UUID deviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            HttpServletRequest request) {
        // USER and ADMIN can access
        securityService.requireRole(request, Role.USER, Role.ADMIN);
        
        DailyEnergyConsumptionDTO consumption = hourlyEnergyConsumptionService.getDailyConsumption(deviceId, date);
        return ResponseEntity.ok(consumption);
    }
}

