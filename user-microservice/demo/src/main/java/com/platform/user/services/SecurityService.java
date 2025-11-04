package com.platform.user.services;

import com.platform.user.entities.Role;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class SecurityService {
    
    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String USER_ROLE_HEADER = "X-User-Role";
             
    public UUID getUserId(HttpServletRequest request) {
        String userIdHeader = request.getHeader(USER_ID_HEADER);
        if (userIdHeader == null || userIdHeader.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing X-User-Id header");
        }
        try {
            return UUID.fromString(userIdHeader);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid X-User-Id header format");
        }
    }
               
    public Role getUserRole(HttpServletRequest request) {
        String roleHeader = request.getHeader(USER_ROLE_HEADER);
        if (roleHeader == null || roleHeader.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing X-User-Role header");
        }
        try {
            return Role.valueOf(roleHeader.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid X-User-Role header: " + roleHeader);
        }
    }

    public boolean isAdmin(HttpServletRequest request) {
        Role role = getUserRole(request);
        return role == Role.ADMIN;
    }
    
    public void requireAdmin(HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin role required");
        }
    }
    
    public void requireRole(HttpServletRequest request, Role... allowedRoles) {
        Role userRole = getUserRole(request);
        for (Role allowedRole : allowedRoles) {
            if (userRole == allowedRole) {
                return;
            }
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Insufficient permissions");
    }

    public void requireUser(HttpServletRequest request, UUID allowedUserId) {
        UUID userId = getUserId(request);
        if (!userId.equals(allowedUserId) && !isAdmin(request)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
    }
}

