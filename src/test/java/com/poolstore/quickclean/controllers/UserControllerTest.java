package com.poolstore.quickclean.controllers;

import com.poolstore.quickclean.dtos.LoginRequest;
import com.poolstore.quickclean.dtos.RegisterRequest;
import com.poolstore.quickclean.models.Message;
import com.poolstore.quickclean.models.Timer;
import com.poolstore.quickclean.models.User;
import com.poolstore.quickclean.services.MessageService;
import com.poolstore.quickclean.services.TimerService;
import com.poolstore.quickclean.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private TimerService timerService;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private UserController userController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testCheckCredentials_UserDoesNotExist() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("nonexistent", "password");

       when(userService.findByCredentials(loginRequest.getUsername())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Boolean> response = userController.checkCredentials(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotEquals(Boolean.TRUE, response.getBody());
    }

    @Test
    public void testCheckCredentials_PasswordsDoNotMatch() {
        // Arrange
        String username = "existingUser";
        String correctPassword = "correctPassword";
        String incorrectPassword = "incorrectPassword";
        LoginRequest loginRequest = new LoginRequest(username, incorrectPassword);

        User existingUser = new User();
        existingUser.setUsername(username);
        existingUser.setPassword(correctPassword);

        when(userService.findByCredentials(username)).thenReturn(Optional.of(existingUser));

        // Act
        ResponseEntity<Boolean> response = userController.checkCredentials(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotEquals(Boolean.TRUE, response.getBody());
    }

    @Test
    public void testCheckCredentials_CredentialsMatch() {
        // Arrange
        String username = "existingUser";
        String password = "correctPassword";
        LoginRequest loginRequest = new LoginRequest(username, password);
        User existingUser = new User();
        existingUser.setUsername(username);
        existingUser.setPassword(password);


        when(userService.findByCredentials(username)).thenReturn(Optional.of(existingUser));

        // Act
        ResponseEntity<Boolean> response = userController.checkCredentials(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Boolean.TRUE, response.getBody());
    }

    @Test
    public void testRegister() throws Exception {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Doe");
        registerRequest.setEmail("john.doe@example.com");
        registerRequest.setBirthDate(new Date());
        registerRequest.setPassword("password123");
        registerRequest.setUsername("johndoe");

        User createdUser = new User();
        createdUser.setFirstName(registerRequest.getFirstName());
        createdUser.setLastName(registerRequest.getLastName());
        createdUser.setEmail(registerRequest.getEmail());
        createdUser.setBirthDate(registerRequest.getBirthDate());
        createdUser.setPassword(registerRequest.getPassword());
        createdUser.setUsername(registerRequest.getUsername());
        createdUser.setProfilePicture("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR82DN9JU-hbIhhkPR-AX8KiYzA4fBMVwjLAG82fz7GLg&s");
        createdUser.setPokeBalls(20);

        Timer timer = new Timer();
        LocalDateTime localDate = LocalDateTime.now().minusDays(1);
        timer.setPrevDate(localDate);

        timer.setUser(createdUser);


        Message message = new Message();
        message.setText("Welcome to our Pokemon Trading App!" +
                " We're thrilled to have you join our community of trainers and collectors." +
                " Here, you can trade, message, and collect your favorite Pokemon with fellow enthusiasts" +
                " from all over the world. Below you will find an example of the trading system. Thank you for joining us and we look forward to seeing" +
                " your collection grow!");
        message.setUserPokemon("Umbreon Example");
        message.setTradePokemon("Vileplume Example");
        message.setUserPokemonImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS--yrrnAfZUHMSYNkzIbskqJNBn7weqJOqoSPsAGQgJAYJFzghSvnBWTmPBIiBjTThCtE&usqp=CAU");
        message.setTradePokemonImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSMWK3q3UDNpy3aK7TvWBfbzzgGrEzRh7LiJQ&usqp=CAU");
        message.setTraded(true);
        message.setCurrentUsername("The Pokemon Trading Team");
        message.setUser(createdUser);



        when(userService.save(any(User.class))).thenReturn(createdUser);
        when(timerService.saveTimer(any(Timer.class))).thenReturn(timer);
        when(messageService.save(any(Message.class))).thenReturn(message);


        // Act
        ResponseEntity<User> response = userController.register(registerRequest);


        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(createdUser.getBirthDate() , Objects.requireNonNull(response.getBody()).getBirthDate());
        assertSame(createdUser.getFirstName() , Objects.requireNonNull(response.getBody()).getFirstName());
        assertSame(createdUser.getLastName() , Objects.requireNonNull(response.getBody()).getLastName());
        assertSame(createdUser.getUsername() , Objects.requireNonNull(response.getBody()).getUsername());
        assertSame(createdUser.getPassword() , Objects.requireNonNull(response.getBody()).getPassword());
        assertSame(createdUser.getProfilePicture() , Objects.requireNonNull(response.getBody()).getProfilePicture());
        assertSame(createdUser.getPokeBalls() , Objects.requireNonNull(response.getBody()).getPokeBalls());
        assertSame(createdUser, Objects.requireNonNull(response.getBody()));
        assertEquals(createdUser, response.getBody());

        // Capture the arguments passed to the saveTimer and save methods
        ArgumentCaptor<Timer> timerCaptor = ArgumentCaptor.forClass(Timer.class);
        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);

        verify(timerService).saveTimer(timerCaptor.capture());
        verify(messageService).save(messageCaptor.capture());

        Timer savedTimer = timerCaptor.getValue();
        Message savedMessage = messageCaptor.getValue();

        // Assert individual properties of the captured Timer object
        assertEquals(timer.getId(), savedTimer.getId());

        // Assert individual properties of the captured Message object
        assertEquals(message.getText(), savedMessage.getText());


        // Verify that the userService.save() method was called with the createdUser object
        verify(userService).save(any(User.class));
        verify(timerService).saveTimer(any(Timer.class));
        verify(messageService).save(any(Message.class));
    }


}
