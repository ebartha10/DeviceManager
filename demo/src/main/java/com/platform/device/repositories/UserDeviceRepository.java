package com.platform.device.repositories;

import com.platform.device.entities.UserDevice;
import com.platform.device.entities.primaryKeys.UserDeviceId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserDeviceRepository extends CrudRepository<UserDevice, UserDeviceId> {
    List<UserDevice> findByIdUserId(UUID userId);
}
