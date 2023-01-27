package com.jjra.chatbackend.service;

import com.jjra.chatbackend.model.Message;

public interface MessageService {

    public Message publishMessageToTopic(Message message);

}
