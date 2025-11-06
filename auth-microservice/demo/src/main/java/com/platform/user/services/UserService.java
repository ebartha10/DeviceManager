package com.platform.user.services;

import com.platform.user.dtos.AuthenticationRequestDTO;
import com.platform.user.dtos.AuthenticationResponseDTO;
import com.platform.user.dtos.RegisterRequestDTO;
import com.platform.user.entities.User;
import com.platform.user.entities.Role;
import com.platform.user.handlers.exceptions.model.UserAlreadyExistsException;
import com.platform.user.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${user-microservice.base-url}")
    private String userServiceBaseUrl;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthenticationResponseDTO register(RegisterRequestDTO request) {
        validateEmailNotExists(request.getEmail());
        
        User user = createUser(request);
        User savedUser = userRepository.save(user);

        propagateUserToUserMicroservice(savedUser, request.getFullName());
        
        String jwtToken = generateTokenForUser(savedUser);
        
        return buildAuthenticationResponse(jwtToken, savedUser.getId(), user.getRole());
    }

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = findUserByEmail(request.getEmail());
        String jwtToken = generateTokenForUser(user);

        return buildAuthenticationResponse(jwtToken, user.getId(), user.getRole());
    }

    private void validateEmailNotExists(String email) {
        if (userRepository.findFirstByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("User already exists with email: " + email);
        }
    }

    private User createUser(RegisterRequestDTO request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(parseRole(request.getRole()));
        return user;
    }

    private Role parseRole(String roleString) {
        if (roleString == null || roleString.isBlank()) {
            return Role.USER;
        }

        try {
            return Role.valueOf(roleString.toUpperCase());
        } catch (IllegalArgumentException ex) {
            LOGGER.warn("Invalid role '{}' provided, defaulting to USER", roleString);
            return Role.USER;
        }
    }

    private void propagateUserToUserMicroservice(User user, String fullName) {
        try {
            String url = userServiceBaseUrl + "/users";
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-User-Id", user.getId().toString());
            headers.set("X-User-Role", "ADMIN");
            
            Map<String, Object> body = Map.of(
                    "id", user.getId(),
                    "email", user.getEmail(),
                    "fullName", fullName != null ? fullName : ""
            );
            
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            
            restTemplate.postForLocation(url, requestEntity);
        } catch (Exception ex) {
            LOGGER.warn("Failed to propagate user {} to user microservice: {}", 
                    user.getId(), ex.getMessage());
        }
    }

    private String generateTokenForUser(User user) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getId());
        extraClaims.put("role", user.getRole().name());
        return jwtService.generateToken(extraClaims, userDetails);
    }

    private User findUserByEmail(String email) {
        return userRepository.findFirstByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }

    private AuthenticationResponseDTO buildAuthenticationResponse(String token, UUID userId, Role role) {
        return AuthenticationResponseDTO.builder()
                .token(token)
                .userId(userId)
                .role(role.toString())
                .build();
    }
}