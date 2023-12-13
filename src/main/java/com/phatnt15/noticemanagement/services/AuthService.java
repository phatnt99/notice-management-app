package com.phatnt15.noticemanagement.services;

import com.phatnt15.noticemanagement.dtos.AuthResponse;
import com.phatnt15.noticemanagement.dtos.UserRequest;
import com.phatnt15.noticemanagement.dtos.UserResponse;

/**
 * The interface Auth service.
 */
public interface AuthService {

    /**
     * Register user user response.
     *
     * @param request the request
     * @return the user response
     */
    UserResponse registerUser(UserRequest request);

    /**
     * Authenticate user auth response.
     *
     * @param request the request
     * @return the auth response
     */
    AuthResponse authenticateUser(UserRequest request);
}
