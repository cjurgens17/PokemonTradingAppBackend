package com.poolstore.quickclean.services;


import com.poolstore.quickclean.models.Message;
import com.poolstore.quickclean.models.User;
import com.poolstore.quickclean.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message save(Message message){
       return messageRepository.save(message);
    }

    public List<Message> getUserMessages(User user){
        List<Message> inbox = new ArrayList<>();

        if(user == null){
            return inbox;
        }

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

    public Optional<Message> findMessageById(Long id){
        return messageRepository.findById(id);
    }

    public void deleteMessageById(Message message){
        messageRepository.deleteById(message.getId());
    }
}
