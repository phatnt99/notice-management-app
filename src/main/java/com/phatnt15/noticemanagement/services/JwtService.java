package com.phatnt15.noticemanagement.services;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

/**
 * The interface Jwt service.
 */
public interface JwtService {

    /**
     * Generate token string.
     *
     * @param username the username
     * @return the string
     */
    String generateToken(String username);

    /**
     * Validate token boolean.
     *
     * @param token       the token
     * @param userDetails the user details
     * @return the boolean
     */
    boolean validateToken(String token, UserDetails userDetails);

    /**
     * Extract subject string.
     *
     * @param token the token
     * @return the string
     */
    String extractSubject(String token);

    /**
     * Extract expiration date.
     *
     * @param token the token
     * @return the date
     */
    Date extractExpiration(String token);
}
