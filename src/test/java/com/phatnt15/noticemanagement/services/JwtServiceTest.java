package com.phatnt15.noticemanagement.services;

import com.phatnt15.noticemanagement.dtos.AuthUserDetail;
import com.phatnt15.noticemanagement.services.impls.JwtServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @InjectMocks
    private JwtServiceImpl jwtService;

    @Test
    public void whenGenerateToken_shouldOk() {
        setUpJwtFields();

        String result = jwtService.generateToken("username");

        assertNotNull(result);
    }

    @Test
    public void whenValidateToken_shouldOk() {
        setUpJwtFields();

        String username = "username";
        AuthUserDetail authUserDetail = new AuthUserDetail(UUID.randomUUID(), username, null);

        String token = jwtService.generateToken(username);

        assertTrue(jwtService.validateToken(token, authUserDetail));
    }

    @Test
    public void whenExtractExpiration_shouldOk() {
        setUpJwtFields();

        String username = "username";
        String token = jwtService.generateToken(username);

        assertNotNull(jwtService.extractExpiration(token));
    }

    @Test
    public void whenExtractSubject_shouldOk() {
        setUpJwtFields();

        String username = "username";
        String token = jwtService.generateToken(username);

        assertNotNull(jwtService.extractSubject(token));
    }

    public void setUpJwtFields() {
        ReflectionTestUtils.setField(jwtService, "jwtSecret", "45467cd691dbf01cb71ec2e093a003ea2fb14843f7b43fffe3ab0744eeb1421e");
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 86400000L);
    }
}
