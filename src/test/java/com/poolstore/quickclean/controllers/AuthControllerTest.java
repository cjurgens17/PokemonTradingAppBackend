package com.poolstore.quickclean.controllers;


import com.poolstore.quickclean.dtos.LoginRequest;
import com.poolstore.quickclean.models.User;
import com.poolstore.quickclean.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest{

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void testLogin_ValidUser_ReturnsOk() {
        // Mocking the authentication service to return a non-empty optional
        User user = new User();
        when(authService.findByCredentials(anyString(), anyString())).thenReturn(Optional.of(user));

        // Creating a login request
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("user1");
        loginRequest.setPassword("password");

        // Calling the login method
        ResponseEntity<User> response = authController.login(loginRequest);

        // Verifying the authentication service was called with the correct credentials
        verify(authService).findByCredentials("user1", "password");

        // Asserting the expected response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void testLogin_InvalidUser_ReturnsBadRequest() {
        // Mocking the authentication service to return an empty optional
        when(authService.findByCredentials(anyString(), anyString())).thenReturn(Optional.empty());

        // Creating a login request
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("user1");
        loginRequest.setPassword("password");

        // Calling the login method
        ResponseEntity<User> response = authController.login(loginRequest);

        // Verifying the authentication service was called with the correct credentials
        verify(authService).findByCredentials("user1", "password");

        // Asserting the expected response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }
}
