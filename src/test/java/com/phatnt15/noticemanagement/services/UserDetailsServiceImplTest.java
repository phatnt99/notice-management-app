package com.phatnt15.noticemanagement.services;

import com.phatnt15.noticemanagement.entities.User;
import com.phatnt15.noticemanagement.repositories.UserRepository;
import com.phatnt15.noticemanagement.services.impls.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void whenLoadUserByUsername_shouldOk() {
        String username = "username";

        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));

        UserDetails result = userDetailsService.loadUserByUsername(username);

        assertEquals(username, result.getUsername());
    }

}
