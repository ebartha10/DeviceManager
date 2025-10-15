package com.platform.device.entities.primaryKeys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDeviceId implements Serializable {

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "device_id")
    private UUID deviceId;
}
