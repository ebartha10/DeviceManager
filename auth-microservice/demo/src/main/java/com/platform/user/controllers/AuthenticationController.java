package com.platform.user.controllers;


import com.platform.user.dtos.AuthenticationRequestDTO;
import com.platform.user.dtos.AuthenticationResponseDTO;
import com.platform.user.dtos.RegisterRequestDTO;
import com.platform.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO request) {
        AuthenticationResponseDTO response = userService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestDTO request) {
        AuthenticationResponseDTO response = userService.authenticate(request);
        return ResponseEntity.ok(response);
    }
}