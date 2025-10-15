package com.platform.user.controllers;

import com.platform.user.dtos.UserDTO;
import com.platform.user.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/get-all")
    @ResponseBody
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<UserDTO> getUserById(@RequestParam UUID id){
        UserDTO user = this.userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO user){
        UserDTO createdUser = this.userService.createUser(user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("?{id}")
                .buildAndExpand(createdUser.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdUser);
    }

    @PutMapping
    @ResponseBody
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO user){
        UserDTO newUser = this.userService.updateUser(user);
        return ResponseEntity.accepted().body(newUser);
    }

    @DeleteMapping
    @ResponseBody
    public ResponseEntity<String> deleteUserById(@RequestParam UUID id){
        this.userService.deleteById(id);
        return ResponseEntity.ok("Deletion Successful");
    }
}

