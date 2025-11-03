package com.platform.user.controllers;


import com.platform.user.dtos.AuthenticationRequestDTO;
import com.platform.user.dtos.AuthenticationResponseDTO;
import com.platform.user.dtos.RegisterRequestDTO;
import com.platform.user.services.JWTService;
import com.platform.user.services.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationController {
    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;

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

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }

        String token = authHeader.substring(7);
        try {
            Claims claims = jwtService.extractClaim(token, claims1 -> claims1);
            Object userIdObj = claims.get("userId");
            Object roleObj = claims.get("role");
            
            String userId = userIdObj != null ? userIdObj.toString() : null;
            String role = roleObj != null ? roleObj.toString() : null;

            if (userId == null || role == null) {
                return ResponseEntity.status(401).build();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-User-Id", userId);
            headers.set("X-User-Role", role);

            return ResponseEntity.ok().headers(headers).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).build();
        }
    }
}