package com.pokemontrading.server.services;


import com.pokemontrading.server.models.Message;
import com.pokemontrading.server.models.User;
import com.pokemontrading.server.repository.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @InjectMocks
    private MessageService messageService;

    @Mock
    private MessageRepository messageRepository;


    @Test
    void getUserMessages_ReturnsInboxForUser() {
        // Arrange
        User existingUser = new User();
        when(messageRepository.findAll()).thenReturn(List.of(
                createMessage("message1", existingUser),
                createMessage("message2", existingUser),
                createMessage("message3", null),
                createMessage("message4", existingUser)
        ));

        // Perform the method call
        List<Message> inbox = messageService.getUserMessages(existingUser);

        // Verify the result
        assertEquals(3, inbox.size());
        assertEquals("message1", inbox.get(0).getText());
        assertEquals("message2", inbox.get(1).getText());
    }

    // Helper method to create a Pokemon with a given name and user
    private Message createMessage(String text, User user) {
        Message message = new Message();
        message.setText(text);
        message.setUser(user);
        return message;
    }

}
