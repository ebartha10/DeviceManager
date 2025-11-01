package com.platform.user.dtos.builders;

import com.platform.user.dtos.UserDTO;
import com.platform.user.entities.User;

public class UserBuilder {

    public static UserDTO fromPersistance(User user) {
        return new UserDTO(user.getId(), user.getRole(), user.getEmail());
    }

    public static User toUserEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setRole(userDTO.getRole());
        user.setEmail(userDTO.getEmail());
        return user;
    }
}
