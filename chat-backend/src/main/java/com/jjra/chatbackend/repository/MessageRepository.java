package com.jjra.chatbackend.repository;

import com.jjra.chatbackend.model.Message;

import java.util.Map;

public interface MessageRepository {

    Map<String, Message> saveMessage(Message message);

    public Map<String, Message> getMessages();
}
