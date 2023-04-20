package com.poolstore.quickclean.services;


import com.poolstore.quickclean.models.Message;
import com.poolstore.quickclean.repository.MessageRepository;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void save(Message message){
        messageRepository.save(message);
    }
}
