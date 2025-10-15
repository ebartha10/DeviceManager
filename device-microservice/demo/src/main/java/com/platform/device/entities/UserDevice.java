package com.platform.device.entities;

import com.platform.device.entities.primaryKeys.UserDeviceId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Reference;

import java.util.UUID;

@Entity
@Table(name = "user_devices")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDevice {

    @EmbeddedId
    private UserDeviceId id;

    @ManyToOne
    @JoinColumn(name = "device_id", insertable = false, updatable = false)
    private Device device;

    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "serial_number", unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String serialNumber;
}
