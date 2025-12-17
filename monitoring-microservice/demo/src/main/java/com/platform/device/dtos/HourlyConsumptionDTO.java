package com.platform.device.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@lombok.ToString
public class HourlyConsumptionDTO {
    private LocalDateTime hourTimestamp;
    private Double consumption;
}
