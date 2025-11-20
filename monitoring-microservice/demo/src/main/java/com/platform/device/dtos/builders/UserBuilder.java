package com.platform.device.dtos.builders;

import com.platform.device.dtos.UserDTO;
import com.platform.device.entities.User;

public class UserBuilder {

    public static UserDTO fromPersistence(com.platform.device.entities.User givenUser){
        return new UserDTO(givenUser.getId());
    }

    public static User toEntity(UserDTO givenUserDTO){
        return new User(givenUserDTO.getId());
    }
}
