package com.phatnt15.noticemanagement.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phatnt15.noticemanagement.dtos.AuthResponse;
import com.phatnt15.noticemanagement.dtos.UserRequest;
import com.phatnt15.noticemanagement.dtos.UserResponse;
import com.phatnt15.noticemanagement.exceptions.UserAlreadyRegisteredException;
import com.phatnt15.noticemanagement.services.AuthService;

import com.phatnt15.noticemanagement.services.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * The type Auth controller test.
 */
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthService authService;

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * When register user should ok.
     *
     * @throws Exception the exception
     */
    @Test
    public void whenRegisterUser_shouldOk() throws Exception {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("user");
        userRequest.setPassword("user");

        UserResponse userResponse = new UserResponse();
        userResponse.setUsername("user");

        when(authService.registerUser(any(UserRequest.class))).thenReturn(userResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message.username").value(userRequest.getUsername()));
    }

    /**
     * When register user should throw user already registered exception.
     *
     * @throws Exception the exception
     */
    @Test
    public void whenRegisterUser_shouldThrowUserAlreadyRegisteredException() throws Exception {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("user");
        userRequest.setPassword("user");

        String response = String.format("username %s was already taken", userRequest.getUsername());

        when(authService.registerUser(any(UserRequest.class)))
                .thenThrow(new UserAlreadyRegisteredException(
                        response));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(response));
    }


    /**
     * When login with right credential should ok.
     *
     * @throws Exception the exception
     */
    @Test
    public void whenLogin_withRightCredential_shouldOk() throws Exception {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("user");
        userRequest.setPassword("user");

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken("sample-token");

        when(authService.authenticateUser(any(UserRequest.class))).thenReturn(authResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message.accessToken").value(authResponse.getAccessToken()));
    }

    /**
     * When login with wrong credential should fail.
     *
     * @throws Exception the exception
     */
    @Test
    public void whenLogin_withWrongCredential_shouldFail() throws Exception {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("user");
        userRequest.setPassword("user");

        String response = "invalid username or password";

        when(authService.authenticateUser(any(UserRequest.class))).thenThrow(new UsernameNotFoundException(response));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(response));
    }

    /**
     * When login with bad request should fail.
     *
     * @throws Exception the exception
     */
    @Test
    public void whenLogin_withBadRequest_shouldFail() throws Exception {
        UserRequest userRequest = new UserRequest();

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken("sample-token");

        when(authService.authenticateUser(any(UserRequest.class))).thenReturn(authResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}
