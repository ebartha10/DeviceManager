package com.platform.device.services;

import com.platform.device.dtos.UserDTO;
import com.platform.device.dtos.builders.UserBuilder;
import com.platform.device.entities.User;
import com.platform.device.handlers.exceptions.model.ResourceNotFoundException;
import com.platform.device.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public List<UserDTO> getAllUsers() {
        List<User> users = (List<User>) this.userRepository.findAll();
        return users.stream().map(UserBuilder::fromPersistance).toList();
    }

    public UserDTO getUserById(UUID id) {
        Optional<User> user = this.userRepository.findById(id);
        if(user.isEmpty()) {
            LOGGER.error("User with id {} was not found in db", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }

        return UserBuilder.fromPersistance(user.get());
    }

    public UserDTO createUser(UserDTO user) {
        User newUser = UserBuilder.toUserEntity(user);
        newUser = this.userRepository.save(newUser);
        LOGGER.debug("User with id {} was inserted in db", newUser.getId());
        return UserBuilder.fromPersistance(newUser);
    }

    public UserDTO updateUser(UserDTO givenUser) {
        Optional<User> user = this.userRepository.findById(givenUser.getId());
        if(user.isEmpty()) {
            LOGGER.error("User with id {} was not found in db", givenUser.getId());
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + givenUser.getId());
        }

        User existingUser = user.get();
        existingUser.setFullName(givenUser.getFullName());
        existingUser.setEmail(givenUser.getEmail());

        existingUser = userRepository.save(existingUser);
        LOGGER.debug("User with id {} was updated in db", existingUser.getId());

        return UserBuilder.fromPersistance(existingUser);
    }

    public void deleteById(UUID id) {
        Optional<User> user = this.userRepository.findById(id);
        if(user.isEmpty()) {
            LOGGER.error("User with id {} was not found in db", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }

        this.userRepository.deleteById(id);
        LOGGER.debug("User with id {} was deleted from db", id);
    }

}