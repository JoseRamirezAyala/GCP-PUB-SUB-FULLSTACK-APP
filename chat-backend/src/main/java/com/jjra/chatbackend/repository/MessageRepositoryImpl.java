package com.jjra.chatbackend.repository;

import com.jjra.chatbackend.model.Message;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class MessageRepositoryImpl implements MessageRepository {

    Map<String, Message> messages = new HashMap<>();
    @Override
    public Map<String, Message> saveMessage(Message message) {
        messages.put(message.getId(), message);
        return messages;
    }

    @Override
    public Map<String, Message> getMessages() {
        return messages;
    }
}
