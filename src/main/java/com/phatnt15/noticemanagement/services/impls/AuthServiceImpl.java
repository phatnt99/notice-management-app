package com.phatnt15.noticemanagement.services.impls;

import com.phatnt15.noticemanagement.dtos.AuthResponse;
import com.phatnt15.noticemanagement.dtos.UserRequest;
import com.phatnt15.noticemanagement.dtos.UserResponse;
import com.phatnt15.noticemanagement.entities.User;
import com.phatnt15.noticemanagement.exceptions.UserAlreadyRegisteredException;
import com.phatnt15.noticemanagement.mappers.UserMapper;
import com.phatnt15.noticemanagement.repositories.UserRepository;
import com.phatnt15.noticemanagement.services.AuthService;
import com.phatnt15.noticemanagement.services.JwtService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * The type Auth service.
 */
@Service
@AllArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public UserResponse registerUser(UserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyRegisteredException(String.format("username [%s] was already taken", request.getUsername()));
        }

        User user = userMapper.toEntity(request);
        // encode password with PasswordEncoder
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userMapper.toResponse(userRepository.save(user));
    }

    public AuthResponse authenticateUser(UserRequest request) {
        AuthResponse authResponse = new AuthResponse();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            authResponse.setAccessToken(jwtService.generateToken(request.getUsername()));
        } catch (AuthenticationException e) {
            throw new UsernameNotFoundException("invalid username or password");
        }

        return authResponse;
    }
}
