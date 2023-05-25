package com.pokemontrading.server.controllers;

import com.pokemontrading.server.dtos.LoginRequest;
import com.pokemontrading.server.dtos.RegisterRequest;
import com.pokemontrading.server.dtos.Trade;
import com.pokemontrading.server.exceptions.NotFoundException;
import com.pokemontrading.server.models.Message;
import com.pokemontrading.server.models.Pokemon;
import com.pokemontrading.server.models.Timer;
import com.pokemontrading.server.models.User;
import com.pokemontrading.server.services.MessageService;
import com.pokemontrading.server.services.PokemonService;
import com.pokemontrading.server.services.TimerService;
import com.pokemontrading.server.services.UserService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private TimerService timerService;

    @Mock
    private MessageService messageService;

    @Mock
    private PokemonService pokemonService;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private UserController userController;

    @Autowired
    private MockMvc mockMvc;


    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }
//----------------------------------------------------------------------------------------
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

        // Verify that the userService.save() method was called with the createdUser object
        verify(userService).save(any(User.class));
        verify(timerService).saveTimer(any(Timer.class));
        verify(messageService).save(any(Message.class));
    }

    @Test
    void testUserLogin_ValidCredentials() {
        // Mocking the userService.findByCredentials() method to return a user
        String username = "testuser";
        String password = "testpassword";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        when(userService.findByCredentials(username, password)).thenReturn(Optional.of(user));

        // Calling the userLogin() method
        LoginRequest loginRequest = new LoginRequest(username, password); // Assuming LoginRequest class exists
        ResponseEntity<User> responseEntity = userController.userLogin(loginRequest);

        // Verifying the userService.findByCredentials() method was called with the correct arguments
        verify(userService).findByCredentials(username, password);

        // Verifying the expected response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(user, responseEntity.getBody());
    }

    @Test
    void testUserLogin_InvalidCredentials() {
        //setting up proxy credentials
        String username = "testuser";
        String password = "testpassword";

        when(userService.findByCredentials(username, password)).thenReturn(Optional.empty());

        // Calling the userLogin() method
        LoginRequest loginRequest = new LoginRequest(username, password); // Assuming LoginRequest class exists
        ResponseEntity<User> responseEntity = userController.userLogin(loginRequest);

        // Verifying the userService.findByCredentials() method was called with the correct arguments
        verify(userService).findByCredentials(username, password);

        // Verifying the expected response
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
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
    void testGetUserPokemon_ValidUserId() {
       //Arrange
        Long userId = 1L;
        User user = new User(); // Assuming User class exists
        user.setId(userId);

        when(userService.findUserById(userId)).thenReturn(Optional.of(user));


        List<Pokemon> pokemonList = new ArrayList<>();
        Pokemon pokemonOne = new Pokemon();
        pokemonOne.setName("Pikachu");
        Pokemon pokemonTwo = new Pokemon();
        pokemonTwo.setName("Charizard");
        pokemonList.add(pokemonOne);
        pokemonList.add(pokemonTwo);
        user.setUserPokemon(pokemonList);

        when(pokemonService.getUserPokemon(user)).thenReturn(pokemonList);

        // Calling the getUserPokemon() method
        ResponseEntity<List<Pokemon>> responseEntity = userController.getUserPokemon(userId);

        // Verifying the userService.findUserById() method was called with the correct argument
        verify(userService).findUserById(userId);

        // Verifying the pokemonService.getUserPokemon() method was called with the correct user argument
        verify(pokemonService).getUserPokemon(user);

        // Verifying the expected response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(pokemonList, responseEntity.getBody());
    }

    @Test
    void testGetUserPokemon_InvalidUserId() {
        Long userId = 1L;

        when(userService.findUserById(userId)).thenReturn(Optional.empty());

        // Calling the getUserPokemon() method
        ResponseEntity<List<Pokemon>> responseEntity = userController.getUserPokemon(userId);

        // Verifying the userService.findUserById() method was called with the correct argument
        verify(userService).findUserById(userId);

        // Verifying the expected response
       assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
       assertNull(responseEntity.getBody());
    }

    @Test
    void testGetUserMessages_ValidUserId() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userService.findUserById(userId)).thenReturn(Optional.of(user));


        List<Message> messageList = new ArrayList<>();
        Message messageOne = new Message();
        Message messageTwo = new Message();
        messageOne.setText("Test messageOne");
        messageTwo.setText("Test messageTwo");
        messageList.add(messageOne);
        messageList.add(messageTwo);
        user.setInbox(messageList);

        when(messageService.getUserMessages(user)).thenReturn(messageList);

        // Calling the getUserMessages() method
        ResponseEntity<List<Message>> responseEntity = userController.getUserMessages(userId);

        // Verifying the userService.findUserById() method was called with the correct argument
        verify(userService).findUserById(userId);

        // Verifying the messageService.getUserMessages() method was called with the correct user argument
        verify(messageService).getUserMessages(user);

        // Verifying the expected response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(messageList, responseEntity.getBody());
    }

    @Test
    void testGetUserMessages_InvalidUserId() {
       //Arrange
        Long userId = 1L;

        when(userService.findUserById(userId)).thenReturn(Optional.empty());

        // Calling the getUserMessages() method
        ResponseEntity<List<Message>> responseEntity = userController.getUserMessages(userId);

        // Verifying the userService.findUserById() method was called with the correct argument
        verify(userService).findUserById(userId);

        // Verifying the expected response
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    void testUpdateIsTraded_ValidUsernameAndMessage() {
        // Arrange
        String username = "testuser";
        User user = new User();
        user.setUsername(username);

        when(userService.findByCredentials(username)).thenReturn(Optional.of(user));


        Message updatedMessage = new Message();
        updatedMessage.setId(1L);
        updatedMessage.setTraded(false);
        updatedMessage.setText("original message");

        //check false
        assertFalse(updatedMessage.isTraded());
        //update to true
        updatedMessage.setTraded(true);

        when(messageService.save(any(Message.class))).thenReturn(updatedMessage);

        ResponseEntity<Message> responseEntity = userController.updateIsTraded(updatedMessage, username);

        // Verifying the userService.findByCredentials() method was called with the correct argument
        verify(userService).findByCredentials(username);

        // Verifying the messageService.save() method was called with the updated message
        verify(messageService).save(any(Message.class));

        // Verifying the expected response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedMessage, responseEntity.getBody());
        assertTrue(updatedMessage.isTraded());
    }

    @Test
    void testUpdateIsTraded_InvalidUsername() {
        // Arrange
        String username = "testuser";

        when(userService.findByCredentials(username)).thenReturn(Optional.empty());

        // Calling the updateIsTraded() method
        Message message = new Message();
        message.setId(1L);

        ResponseEntity<Message> responseEntity = userController.updateIsTraded(message, username);

        // Verifying the userService.findByCredentials() method was called with the correct argument
        verify(userService).findByCredentials(username);

        // Verifying the expected response
       assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
       assertNull(responseEntity.getBody());
    }


    @Test
    void testGetUserInfo_ValidUserId() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setFirstName("test");

        when(userService.findUserById(userId)).thenReturn(Optional.of(user));

        // Calling the getUserInfo() method
        ResponseEntity<User> response = userController.getUserInfo(userId);

        // Verifying the userService.findUserById() method was called with the correct argument
        verify(userService).findUserById(userId);

        // Verifying the expected response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        assertSame(user.getFirstName(), Objects.requireNonNull(response.getBody()).getFirstName());
    }
    @Test
    void testGetUserInfo_InvalidUserId() {
        // Arrange
        Long userId = 1L;

        when(userService.findUserById(userId)).thenReturn(Optional.empty());

        // Calling the getUserInfo() method
        ResponseEntity<User> responseEntity = userController.getUserInfo(userId);

        // Verifying the userService.findUserById() method was called with the correct argument
        verify(userService).findUserById(userId);

      assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
      assertNull(responseEntity.getBody());
    }

    @Test
    void testGetUserByUsername_ValidUsername() {
        // Arrange
        String username = "testuser";
        User user = new User();

        when(userService.findByCredentials(username)).thenReturn(Optional.of(user));

        // Calling the getUserByUsername() method
        ResponseEntity<User> response = userController.getUserByUsername(username);

        // Verifying the userService.findByCredentials() method was called with the correct argument
        verify(userService).findByCredentials(username);

        // Verifying the expected response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetUserByUsername_InvalidUsername() {
        // Arrange
        String username = "testuser";

        when(userService.findByCredentials(username)).thenReturn(Optional.empty());

        // Calling the getUserByUsername() method
        ResponseEntity<User> responseEntity = userController.getUserByUsername(username);

        // Verifying the userService.findByCredentials() method was called with the correct argument
        verify(userService).findByCredentials(username);

        // Verifying the expected response
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    void testGetAllUsers_UsersNotEmpty() {
        // Arrange
        List<User> users = new ArrayList<>();
        User user1 = new User();
        User user2 = new User();
        user1.setId(1L);
        user2.setId(2L);
        users.add(user1);
        users.add(user2);

        when(userService.getUsers()).thenReturn(users);

        // Calling the getAllUsers() method
        ResponseEntity<List<User>> response = userController.getAllUsers();

        // Verifying the userService.getUsers() method was called
        verify(userService).getUsers();

        // Verifying the expected response
       assertEquals(HttpStatus.OK, response.getStatusCode());
       assertEquals(users, response.getBody());
    }

    @Test
    void testGetAllUsers_UsersEmpty() {
        // Mocking the userService.getUsers() method to return an empty list
        List<User> emptyList = new ArrayList<>();

        when(userService.getUsers()).thenReturn(emptyList);

        // Calling the getAllUsers() method
        ResponseEntity<List<User>> response = userController.getAllUsers();

        // Verifying the userService.getUsers() method was called
        verify(userService).getUsers();

        // Verifying the expected response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testAddMessage_ValidUsername() {
        // Arrange
        String username = "testuser";
        User user = new User();
        user.setUsername(username);

        when(userService.findByCredentials(username)).thenReturn(Optional.of(user));

        // Calling the addMessage() method
        Message message = new Message();
        message.setId(1L);
        message.setText("test");
        message.setUserPokemon("Charizard");
        message.setTradePokemon("Blastoise");
        message.setUsername("user1");
        message.setTradePokemonImage("test Image");
        message.setUserPokemonImage("test Image");
        message.setCurrentUsername("user2");
        message.setTraded(false);
        message.setUser(user);

        when(messageService.save(any(Message.class))).thenReturn(message);

        ResponseEntity<User> responseEntity = userController.addMessage(username, message);

        // Verifying the userService.findByCredentials() method was called with the correct argument
        verify(userService).findByCredentials(username);

        // Verifying the messageService.save() method was called with the new message
        verify(messageService).save(any(Message.class));

        Optional<Message> verifySameMessage = user.getInbox().stream().findFirst().filter(verifyMessage -> verifyMessage.getId() == 1);


        // Verifying the expected response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(user, responseEntity.getBody());
        assertSame(verifySameMessage,
                Objects.requireNonNull(responseEntity.getBody()).getInbox().stream().findFirst().filter(sameMessage -> sameMessage.getId() == 1)
        );
    }

    @Test
    void testAddMessage_InvalidUsername() {
        // Arrange
        String username = "testuser";

        when(userService.findByCredentials(username)).thenReturn(Optional.empty());

        // Calling the addMessage() method
        Message message = new Message();
        message.setText("Test message");

        // Verifying that NotFoundException is thrown
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            userController.addMessage(username, message);
        });

        // Verifying the exception message
        Assertions.assertEquals("User does not exist", exception.getMessage());

        // Verifying the userService.findByCredentials() method was called with the correct argument
        verify(userService).findByCredentials(username);

        // Verifying the messageService.save() method was not called
        verifyNoInteractions(messageService);
    }

    @Test
    void testDeleteMessage_ValidMessageId() {
        // Arrange
        long messageId = 1L;
        Message message = new Message();
        message.setId(messageId);

        when(messageService.findMessageById(messageId)).thenReturn(Optional.of(message));

        // Calling the deleteMessage() method
        ResponseEntity<Boolean> responseEntity = userController.deleteMessage(message);

        // Verifying the messageService.findMessageById() method was called with the correct argument
        verify(messageService).findMessageById(messageId);

        // Verifying the messageService.deleteMessageById() method was called with the expected message argument
        verify(messageService).deleteMessageById(message);

        // Verifying the expected response
       assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
       assertEquals(true, responseEntity.getBody());
    }

    @Test
    void testDeleteMessage_InvalidMessageId() {
        // Arrange
        long messageId = 1L;
        Message deleteMessage = new Message();
        deleteMessage.setId(messageId);

        when(messageService.findMessageById(deleteMessage.getId())).thenReturn(Optional.empty());

        // Calling the deleteMessage() method
        ResponseEntity<Boolean> response = userController.deleteMessage(deleteMessage);

        // Verifying the messageService.findMessageById() method was called with the correct argument
        verify(messageService).findMessageById(deleteMessage.getId());

        // Verifying no more interactons with message service after finding messageById is called
        verifyNoMoreInteractions(messageService);

        // Verifying the expected response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testCheckUsersPokemon_ValidUsernamesAndPokemon() {
        // Arranging for a valid request -- valid usernames and both users currently have pokemon available
        String username = "user1";
        String currentUsername = "user2";
        String userPokemon = "Charmander";
        String tradePokemon = "Pikachu";
        User user1 = new User();
        user1.setUsername(username);
        User user2 = new User();
        user2.setUsername(currentUsername);
        when(userService.findByCredentials(username)).thenReturn(Optional.of(user1));
        when(userService.findByCredentials(currentUsername)).thenReturn(Optional.of(user2));

        // Mocking the pokemonService.getUserPokemon() method to return pokemon lists
        Pokemon pikachu = new Pokemon();
        pikachu.setName("Pikachu");
        Pokemon charmander = new Pokemon();
        charmander.setName("Charmander");
        List<Pokemon> user1Pokemon = new ArrayList<>();
        user1Pokemon.add(pikachu);
        List<Pokemon> user2Pokemon = new ArrayList<>();
        user2Pokemon.add(charmander);
        user1.setUserPokemon(user1Pokemon);
        user2.setUserPokemon(user2Pokemon);
        when(pokemonService.getUserPokemon(user1)).thenReturn(user1Pokemon);
        when(pokemonService.getUserPokemon(user2)).thenReturn(user2Pokemon);

        // Calling the checkUsersPokemon() method
        ResponseEntity<Boolean> response = userController.checkUsersPokemon(username, currentUsername, userPokemon, tradePokemon);

        // Verifying the userService.findByCredentials() method was called with the correct arguments
        verify(userService).findByCredentials(username);
        verify(userService).findByCredentials(currentUsername);

        // Verifying the pokemonService.getUserPokemon() method was called with the correct user objects
        verify(pokemonService).getUserPokemon(user1);
        verify(pokemonService).getUserPokemon(user2);

        // Verifying that no further interactions occurred with the userService and pokemonService
        verifyNoMoreInteractions(userService, pokemonService);

        // Verifying the expected response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Boolean.TRUE, response.getBody());
    }

    @Test
    void testCheckUsersPokemon_InvalidUser() {
        // Arrange for invalid users-- return an empty optional
        String username = "user1";
        String currentUsername = "user2";
        String userPokemon = "Pikachu";
        String tradePokemon = "Charmander";
        User user1 = new User();
        user1.setUsername(username);
        when(userService.findByCredentials(username)).thenReturn(Optional.of(user1));
        when(userService.findByCredentials(currentUsername)).thenReturn(Optional.empty());

        // Calling the checkUsersPokemon() method
        ResponseEntity<Boolean> response = userController.checkUsersPokemon(username, currentUsername, userPokemon, tradePokemon);

        // Verifying the userService.findByCredentials() method was called with the correct arguments
        verify(userService).findByCredentials(username);
        verify(userService).findByCredentials(currentUsername);

        // Verifying that no further interactions occurred with the userService and pokemonService
        verifyNoMoreInteractions(userService, pokemonService);

        // Verifying the expected response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testCheckUsersPokemon_UserWithoutExpectedPokemon() {
        // Arrange for users having the wrong pokemon
        String username = "user1";
        String currentUsername = "user2";
        String userPokemon = "Pikachu";
        String tradePokemon = "Charmander";
        User user1 = new User();
        user1.setUsername(username);
        User user2 = new User();
        user2.setUsername(currentUsername);
        when(userService.findByCredentials(username)).thenReturn(Optional.of(user1));
        when(userService.findByCredentials(currentUsername)).thenReturn(Optional.of(user2));

        // Mocking the pokemonService.getUserPokemon() method to return pokemon lists
        Pokemon pikachu = new Pokemon();
        pikachu.setName("Pikachu");
        Pokemon squirtle = new Pokemon();
        squirtle.setName("Squirtle");
        List<Pokemon> user1Pokemon = new ArrayList<>();
        user1Pokemon.add(pikachu);
        List<Pokemon> user2Pokemon = new ArrayList<>();
        user2Pokemon.add(squirtle);
        user1.setUserPokemon(user1Pokemon);
        user2.setUserPokemon(user2Pokemon);
        when(pokemonService.getUserPokemon(user1)).thenReturn(user1Pokemon);
        when(pokemonService.getUserPokemon(user2)).thenReturn(user2Pokemon);

        // Calling the checkUsersPokemon() method
        ResponseEntity<Boolean> response = userController.checkUsersPokemon(username, currentUsername, userPokemon, tradePokemon);

        // Verifying the userService.findByCredentials() method was called with the correct arguments
        verify(userService).findByCredentials(username);
        verify(userService).findByCredentials(currentUsername);

        // Verifying the pokemonService.getUserPokemon() method was called with the correct user objects
        verify(pokemonService).getUserPokemon(user1);
        verify(pokemonService).getUserPokemon(user2);

        // Verifying that no further interactions occurred with the userService and pokemonService
        verifyNoMoreInteractions(userService, pokemonService);

        // Verifying the expected response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testTradePokemon_ValidTrade() {
        // Creating a valid Trade object
        Trade trade = new Trade();
        trade.setUsername("user1");
        trade.setCurrentUsername("user2");
        trade.setUserPokemon("Pikachu");
        trade.setTradePokemon("Charmander");

        User user1 = new User();
        User user2 = new User();

        // Mocking the user service to return Optional<User> for both users
        when(userService.findByCredentials(trade.getUsername())).thenReturn(Optional.of((user1)));
        when(userService.findByCredentials(trade.getCurrentUsername())).thenReturn(Optional.of((user2)));

        // Setting up the tradeUser1Pokemon and tradeUser2Pokemon lists
        List<Pokemon> tradeUser1Pokemon = new ArrayList<>();
        Pokemon tradePokemon = new Pokemon();
        tradePokemon.setName("Charmander");
        tradeUser1Pokemon.add(tradePokemon);
        user1.setUserPokemon(tradeUser1Pokemon);

        List<Pokemon> tradeUser2Pokemon = new ArrayList<>();
        Pokemon userPokemon = new Pokemon();
        userPokemon.setName("Pikachu");
        tradeUser2Pokemon.add(userPokemon);
        user2.setUserPokemon(tradeUser2Pokemon);

        // Mocking entityManager to return false to test for merge
        //then swapping users so trade takes place
        when(entityManager.contains(any(Pokemon.class))).thenReturn(false);
        when(entityManager.merge(userPokemon)).thenAnswer(invocation -> {
            Pokemon pokemon = invocation.getArgument(0);
            pokemon.setUser(user1);
            return pokemon; // Return the modified pokemon object
        });
        when(entityManager.merge(tradePokemon)).thenAnswer(invocation -> {
            Pokemon pokemon = invocation.getArgument(0);
            pokemon.setUser(user2);
            return pokemon; // Return the modified pokemon object
        });


        // Mocking the pokemonService.savePokemon() method
        when(pokemonService.savePokemon(any(Pokemon.class))).thenReturn(any(Pokemon.class));

        // Calling the tradePokemon method
        ResponseEntity<List<Pokemon>> response = userController.tradePokemon(trade);

        // Verifying the userService.findByCredentials() was called for both users
        verify(userService, times(2)).findByCredentials(anyString());

        // Verifying the entityManager.contains() was called
        verify(entityManager, times(2)).contains(any(Pokemon.class));
        verify(entityManager, times(2)).merge(any(Pokemon.class));

        // Verifying the pokemonService.savePokemon() was called
        verify(pokemonService, times(2)).savePokemon(any(Pokemon.class));

        // Verifying the interactions
        verifyNoMoreInteractions(entityManager,userService,pokemonService);

        // Asserting the expected response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(user2.getUserPokemon(), response.getBody());
        assertSame(user2.getUserPokemon().get(0).getName(), "Pikachu");
        assertSame(user1.getUserPokemon().get(0).getName(), "Charmander");
        assertSame(tradeUser1Pokemon.get(0).getUser(), user2);
        assertSame(tradeUser2Pokemon.get(0).getUser(), user1);
    }

    @Test
    void testTradePokemon_InvalidUser() {

        // Creating an invalid Trade object with non-existent users
        Trade trade = new Trade();
        trade.setUsername("nonexistentUser1");
        trade.setCurrentUsername("nonexistentUser2");
        trade.setUserPokemon("Pikachu");
        trade.setTradePokemon("Charmander");

        // Mocking the user service to return Optional.empty() for both users
        when(userService.findByCredentials(anyString())).thenReturn(Optional.empty());

        // Calling the tradePokemon method
        ResponseEntity<List<Pokemon>> response = userController.tradePokemon(trade);



        // Verifying the userService.findByCredentials() was called for both users
        verify(userService, times(2)).findByCredentials(anyString());

        // Asserting the expected response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testTradePokemon_InvalidTrade() {
        // Creating a valid Trade object
        Trade trade = new Trade();
        trade.setUsername("user1");
        trade.setCurrentUsername("user2");
        trade.setUserPokemon("Pikachu");
        trade.setTradePokemon("Charmander");

        User user1 = new User();
        User user2 = new User();

        // Mocking the user service to return Optional<User> for both users
        when(userService.findByCredentials(trade.getUsername())).thenReturn(Optional.of((user1)));
        when(userService.findByCredentials(trade.getCurrentUsername())).thenReturn(Optional.of((user2)));

        // Setting up the tradeUser1Pokemon and tradeUser2Pokemon lists
        List<Pokemon> tradeUser1Pokemon = new ArrayList<>();
        Pokemon tradePokemon = new Pokemon();
        tradePokemon.setName("Charmander");
        tradeUser1Pokemon.add(tradePokemon);
        user1.setUserPokemon(tradeUser1Pokemon);

        List<Pokemon> tradeUser2Pokemon = new ArrayList<>();
        Pokemon userPokemon = new Pokemon();
        //making same as user1 so the second for loop in method doesnt execute and as a result only one
        //trade boolean will return true resulting in a bad request
        userPokemon.setName("Charmander");
        tradeUser2Pokemon.add(userPokemon);
        user2.setUserPokemon(tradeUser2Pokemon);

        // Mocking entityManager to return false to test for merge
        //then swapping users so trade takes place
        when(entityManager.contains(any(Pokemon.class))).thenReturn(false);
        when(entityManager.merge(tradePokemon)).thenAnswer(invocation -> {
            Pokemon pokemon = invocation.getArgument(0);
            pokemon.setUser(user2);
            return pokemon; // Return the modified pokemon object
        });

        // Mocking the pokemonService.savePokemon() method
        when(pokemonService.savePokemon(any(Pokemon.class))).thenReturn(any(Pokemon.class));

        // Calling the tradePokemon method
        ResponseEntity<List<Pokemon>> response = userController.tradePokemon(trade);

        // Verifying the userService.findByCredentials() was called for both users
        verify(userService, times(2)).findByCredentials(anyString());

        // Verifying the entityManager.contains() was called
        verify(entityManager).contains(any(Pokemon.class));
        verify(entityManager).merge(any(Pokemon.class));

        // Verifying the pokemonService.savePokemon() was called
        verify(pokemonService).savePokemon(any(Pokemon.class));

        // Verifying the interactions
        verifyNoMoreInteractions(entityManager,userService,pokemonService);

        // Asserting the expected response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateProfilePicture_ValidIdAndProfilePicture() {
        // Arranging setup
        Long userId = 1L;
        String profilePicture = "profile_picture.jpg";

        User user = new User();
        user.setId(userId);

        // Mocking the userService.findUserById() method to return the user
        when(userService.findUserById(userId)).thenReturn(Optional.of(user));

        // Calling the updateProfilePicture method
        ResponseEntity<Boolean> response = userController.updateProfilePicture(userId, profilePicture);


        // Verifying the userService.findUserById() was called with the correct id
        verify(userService).findUserById(userId);

        // Verifying the userService.save() was called with the updated user
        verify(userService).save(user);

        //verifty interactions
        verifyNoMoreInteractions(userService);

        // Asserting the expected response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Boolean.TRUE, response.getBody());
    }

    @Test
    void testUpdateProfilePicture_InvalidId() {
        // Arranging setup
        Long userId = 1L;
        String profilePicture = "profile_picture.jpg";

        // Mocking the userService.findUserById() method to return an empty Optional<User>
        when(userService.findUserById(userId)).thenReturn(Optional.empty());

        // Calling the updateProfilePicture method
        ResponseEntity<Boolean> response = userController.updateProfilePicture(userId, profilePicture);

        // Verifying the userService.findUserById() was called with the correct id
        verify(userService).findUserById(userId);

        //verify interactions
        verifyNoMoreInteractions(userService);

        // Asserting the expected response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testCheckEmail_EmailExists() {
        // Creating test data
        String email = "test@example.com";

        // Mocking the userService.getUsers() method to return a list of users
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setEmail("test@example.com");
        User user2 = new User();
        user2.setEmail("another@example.com");
        users.add(user1);
        users.add(user2);
        when(userService.getUsers()).thenReturn(users);

        // Calling the checkEmail method
        ResponseEntity<Boolean> response = userController.checkEmail(email);

        // Verifying the userService.getUsers() was called
        verify(userService).getUsers();

        // Verifying the interactions
        verifyNoMoreInteractions(userService);

        // Asserting the expected response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Boolean.TRUE, response.getBody());
    }

    @Test
    void testCheckEmail_EmailDoesNotExist() {
        // Creating test data
        String email = "test@example.com";

        // Mocking the userService.getUsers() method to return a list of users
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setEmail("another@example.com");
        User user2 = new User();
        user2.setEmail("yetanother@example.com");
        users.add(user1);
        users.add(user2);
        when(userService.getUsers()).thenReturn(users);

        // Calling the checkEmail method
        ResponseEntity<Boolean> response = userController.checkEmail(email);

        // Verifying the userService.getUsers() was called
        verify(userService).getUsers();

        // Verifying the interactions
        verifyNoMoreInteractions(userService);

        // Asserting the expected response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotEquals(Boolean.TRUE, response.getBody());
    }

    @Test
    void testCheckUsername_UsernameExists() {
        // Creating test data
        String username = "testuser";

        // Mocking the userService.getUsers() method to return a list of users
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setUsername("anotheruser");
        User user2 = new User();
        user2.setUsername("testuser");
        users.add(user1);
        users.add(user2);
        when(userService.getUsers()).thenReturn(users);

        // Calling the checkUsername method
        ResponseEntity<Boolean> response = userController.checkUsername(username);

        // Verifying the userService.getUsers() was called
        verify(userService).getUsers();

        // Verifying the interactions
        verifyNoMoreInteractions(userService);

        // Asserting the expected response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Boolean.TRUE, response.getBody());
    }

    @Test
    void testCheckUsername_UsernameDoesNotExist() {
        // Creating test data
        String username = "nonexistentuser";

        // Mocking the userService.getUsers() method to return an empty list of users
        List<User> users = new ArrayList<>();
        when(userService.getUsers()).thenReturn(users);

        // Calling the checkUsername method
        ResponseEntity<Boolean> response = userController.checkUsername(username);

        // Verifying the userService.getUsers() was called
        verify(userService).getUsers();

        // Verifying the interactions
        verifyNoMoreInteractions(userService);

        // Asserting the expected response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotEquals(Boolean.TRUE, response.getBody());
    }
}
