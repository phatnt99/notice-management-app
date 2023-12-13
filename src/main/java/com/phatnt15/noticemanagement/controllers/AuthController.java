package com.phatnt15.noticemanagement.controllers;

import com.phatnt15.noticemanagement.dtos.UserRequest;
import com.phatnt15.noticemanagement.services.AuthService;
import com.phatnt15.noticemanagement.utils.HttpResponseBuilder;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Auth controller.
 */
@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService userService;

    /**
     * Register response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRequest request) {
        return ResponseEntity.ok(HttpResponseBuilder.toResponse(userService.registerUser(request)));
    }

    /**
     * Login response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserRequest request) {
        return ResponseEntity.ok(HttpResponseBuilder.toResponse(userService.authenticateUser(request)));
    }
}
