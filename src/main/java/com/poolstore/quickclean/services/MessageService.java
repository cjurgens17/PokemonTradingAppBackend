package com.poolstore.quickclean.services;


import com.poolstore.quickclean.models.Message;
import com.poolstore.quickclean.models.User;
import com.poolstore.quickclean.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void save(Message message){
        messageRepository.save(message);
    }

    public List<Message> getUserMessages(User user){

        List<Message> inbox = new ArrayList<>();

        messageRepository.findAll().forEach(message -> {
            if(message.getUser() == null){
                User nullUser = new User();
                message.setUser(nullUser);
            }
            if(message.getUser().equals(user)){
                inbox.add(message);
            }
        });

        return inbox;
    }
}
