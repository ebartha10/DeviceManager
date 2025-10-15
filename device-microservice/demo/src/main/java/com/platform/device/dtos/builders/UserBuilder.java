package com.platform.device.dtos.builders;

import com.platform.device.dtos.UserDTO;
import com.platform.device.entities.User;

public class UserBuilder {

    public static UserDTO fromPersistance(User user) {
        return new UserDTO(user.getId(), user.getFullName(), user.getEmail());
    }

    public static User toUserEntity(UserDTO userDTO) {
        return new User(userDTO.getId(), userDTO.getFullName(), userDTO.getEmail());
    }
}
