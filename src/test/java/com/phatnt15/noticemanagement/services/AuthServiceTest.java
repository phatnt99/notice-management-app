package com.phatnt15.noticemanagement.services;

import com.phatnt15.noticemanagement.dtos.AuthResponse;
import com.phatnt15.noticemanagement.dtos.UserRequest;
import com.phatnt15.noticemanagement.dtos.UserResponse;
import com.phatnt15.noticemanagement.entities.User;
import com.phatnt15.noticemanagement.exceptions.UserAlreadyRegisteredException;
import com.phatnt15.noticemanagement.mappers.UserMapper;
import com.phatnt15.noticemanagement.repositories.UserRepository;
import com.phatnt15.noticemanagement.services.impls.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;


    @Test
    public void whenRegisterUser_shouldOk() {
        String username = "username";
        String password = "password";
        String encodedPassword = "encoded_password";

        UserRequest request = new UserRequest();
        request.setUsername(username);
        request.setPassword(password);

        User entity = new User();
        entity.setUsername(username);
        entity.setPassword(password);

        UserResponse response = new UserResponse();
        response.setUsername(username);

        when(userRepository.existsByUsername(any(String.class))).thenReturn(false);
        when(userMapper.toEntity(any(UserRequest.class))).thenReturn(entity);
        when(passwordEncoder.encode(any(String.class))).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(entity);
        when(userMapper.toResponse(any(User.class))).thenReturn(response);

        UserResponse result = authService.registerUser(request);

        assertEquals(result.getUsername(), username);
    }

    @Test
    public void whenRegisterUser_withExistUsername_shouldFail() {
        String username = "username";
        String password = "password";

        UserRequest request = new UserRequest();
        request.setUsername(username);
        request.setPassword(password);

        User entity = new User();
        entity.setUsername(username);
        entity.setPassword(password);

        when(userRepository.existsByUsername(any(String.class))).thenReturn(true);

        assertThrows(UserAlreadyRegisteredException.class,
                () -> authService.registerUser(request));
    }

    @Test
    public void whenAuthenticateUser_shouldOk() {
        String username = "username";
        String password = "password";
        String token = "sample-token";

        UserRequest request = new UserRequest();
        request.setUsername(username);
        request.setPassword(password);

        Authentication authentication = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(jwtService.generateToken(any(String.class))).thenReturn("sample-token");

        AuthResponse result = authService.authenticateUser(request);

        assertEquals(token, result.getAccessToken());
    }

    @Test
    public void whenAuthenticateUser_withBadCredential_shouldFail() {
        String username = "username";
        String password = "password";

        UserRequest request = new UserRequest();
        request.setUsername(username);
        request.setPassword(password);

        when(authenticationManager.authenticate(any(Authentication.class))).thenThrow(new BadCredentialsException("bad credential"));

        assertThrows(UsernameNotFoundException.class, () -> authService.authenticateUser(request));
    }
}
