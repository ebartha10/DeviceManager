package com.platform.user.dtos.builders;

import com.platform.user.dtos.UserDTO;
import com.platform.user.entities.User;

public class UserBuilder {

    public static UserDTO fromPersistance(User user) {
        return new UserDTO(user.getId(), user.getFullName(), user.getEmail());
    }

    public static User toUserEntity(UserDTO userDTO) {
        return new User(userDTO.getId(), userDTO.getFullName(), userDTO.getEmail());
    }
}
