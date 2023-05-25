package com.pokemontrading.server.controllers;


import com.pokemontrading.server.models.Timer;
import com.pokemontrading.server.models.User;
import com.pokemontrading.server.services.TimerService;
import com.pokemontrading.server.services.UserService;
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

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TimerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private TimerController timerController;

    @Mock
    private TimerService timerService;

    @Mock
    private UserService userService;



    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(timerController).build();
    }

    @Test
    void updateTimer_SuccessfulRequest() {
        // Mock user and timer
        User user = new User();
        Timer timer = new Timer();
        user.setTimer(timer);

        // Mock service methods
        when(userService.findUserById(1L)).thenReturn(Optional.of(user));
        when(timerService.saveTimer(timer)).thenReturn(timer);
        when(userService.save(user)).thenReturn(user);

        // Perform the update
        ResponseEntity<Timer> response = timerController.updateTimer(1L);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(timer.getPrevDate(), Objects.requireNonNull(response.getBody()).getPrevDate());

        // Verify service method invocations
        verify(timerService, times(1)).saveTimer(timer);
        verify(userService, times(1)).save(user);
    }

    @Test
    void updateTimer_BadRequest_UserNotFound() {
        // Mock user not found
        when(userService.findUserById(1L)).thenReturn(Optional.empty());

        // Perform the update
        ResponseEntity<Timer> response = timerController.updateTimer(1L);

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Verify service method invocations
        verify(timerService, never()).saveTimer(any());
        verify(userService, never()).save(any());
    }

    @Test
    void updateTimer_BadRequest_TimerEmpty() {
        // Mock user without timer
        User user = new User();

        // Mock service method
        when(userService.findUserById(1L)).thenReturn(Optional.of(user));

        // Perform the update
        ResponseEntity<Timer> response = timerController.updateTimer(1L);

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());

        // Verify service method invocations
        verify(timerService, never()).saveTimer(any());
        verify(userService, never()).save(any());
    }

    @Test
    void getTimer_SuccessfulRequest() {
        // Mock user and timer
        User user = new User();
        Timer timer = new Timer();
        user.setTimer(timer);

        // Mock service method
        when(userService.findUserById(1L)).thenReturn(Optional.of(user));

        // Perform the request
        ResponseEntity<Timer> response = timerController.getTimer(1L);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(timer, response.getBody());

        // Verify service method invocation
        verify(userService, times(1)).findUserById(1L);
    }

    @Test
    void getTimer_BadRequest_UserNotFound() {
        // Mock user not found
        when(userService.findUserById(1L)).thenReturn(Optional.empty());

        // Perform the request
        ResponseEntity<Timer> response = timerController.getTimer(1L);

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Verify service method invocation
        verify(userService, times(1)).findUserById(1L);
    }

    @Test
    void getTimer_BadRequest_TimerEmpty() {
        // Mock user without timer
        User user = new User();

        // Mock service method
        when(userService.findUserById(1L)).thenReturn(Optional.of(user));

        // Perform the request
        ResponseEntity<Timer> response = timerController.getTimer(1L);

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Verify service method invocation
        verify(userService, times(1)).findUserById(1L);
    }

    @Test
    void addPokeBalls_SuccessfulRequest() {
        // Mock user
        User user = new User();
        user.setPokeBalls(5);

        // Mock service method
        when(userService.findUserById(1L)).thenReturn(Optional.of(user));
        when(userService.save(user)).thenReturn(user);

        // Perform the request
        ResponseEntity<Integer> response = timerController.addPokeBalls(1L, 3);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(8, response.getBody());

        // Verify service method invocations
        verify(userService, times(1)).findUserById(1L);
        verify(userService, times(1)).save(user);
    }

    @Test
    void addPokeBalls_BadRequest_UserNotFound() {
        // Mock user not found
        when(userService.findUserById(1L)).thenReturn(Optional.empty());

        // Perform the request
        ResponseEntity<Integer> response = timerController.addPokeBalls(1L, 3);

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Verify service method invocation
        verify(userService, times(1)).findUserById(1L);
        verify(userService, never()).save(any());
    }

    @Test
    void deletePokeBalls_SuccessfulRequest() {
        // Mock user
        User user = new User();
        user.setPokeBalls(5);

        // Mock service method
        when(userService.findUserById(1L)).thenReturn(Optional.of(user));
        when(userService.save(user)).thenReturn(user);

        // Perform the request
        ResponseEntity<Integer> response = timerController.deletePokeBalls(1L, 3);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody());

        // Verify service method invocations
        verify(userService, times(1)).findUserById(1L);
        verify(userService, times(1)).save(user);
    }

    @Test
    void deletePokeBalls_BadRequest_UserNotFound() {
        // Mock user not found
        when(userService.findUserById(1L)).thenReturn(Optional.empty());

        // Perform the request
        ResponseEntity<Integer> response = timerController.deletePokeBalls(1L, 3);

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Verify service method invocation
        verify(userService, times(1)).findUserById(1L);
        verify(userService, never()).save(any());
    }

}
