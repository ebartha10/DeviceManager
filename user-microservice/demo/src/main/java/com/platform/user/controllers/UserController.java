package com.platform.user.controllers;

import com.platform.user.dtos.UserDTO;
import com.platform.user.entities.Role;
import com.platform.user.services.SecurityService;
import com.platform.user.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
    
    @Autowired
    private SecurityService securityService;

    @GetMapping("/get-all")
    @ResponseBody
    public ResponseEntity<List<UserDTO>> getAllUsers(HttpServletRequest request){
        // USER and ADMIN can access
        securityService.requireRole(request, Role.USER, Role.ADMIN);
        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<UserDTO> getUserById(@RequestParam UUID id, HttpServletRequest request){
        // USER and ADMIN can access
        securityService.requireRole(request, Role.USER, Role.ADMIN);
        UserDTO user = this.userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO user, HttpServletRequest request){
        // ADMIN only
        securityService.requireAdmin(request);
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
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO user, HttpServletRequest request){
        // USER and ADMIN can access
        securityService.requireRole(request, Role.USER, Role.ADMIN);
        securityService.requireUser(request, user.getId());
        UserDTO newUser = this.userService.updateUser(user);
        return ResponseEntity.accepted().body(newUser);
    }

    @DeleteMapping
    @ResponseBody
    public ResponseEntity<String> deleteUserById(@RequestParam UUID id, HttpServletRequest request){
        // ADMIN only
        securityService.requireAdmin(request);
        this.userService.deleteById(id);
        return ResponseEntity.ok("Deletion Successful");
    }
}

