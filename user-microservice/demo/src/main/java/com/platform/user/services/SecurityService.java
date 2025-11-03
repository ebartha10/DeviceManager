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
    
    /**
     * Gets the userId from the X-User-Id header
     */
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
    
    /**
     * Gets the user role from the X-User-Role header
     */
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
    
    /**
     * Checks if the user has admin role
     */
    public boolean isAdmin(HttpServletRequest request) {
        Role role = getUserRole(request);
        return role == Role.ADMIN;
    }
    
    /**
     * Verifies that the user has admin role, throws exception if not
     */
    public void requireAdmin(HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin role required");
        }
    }
    
    /**
     * Verifies that the user has one of the allowed roles
     */
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

