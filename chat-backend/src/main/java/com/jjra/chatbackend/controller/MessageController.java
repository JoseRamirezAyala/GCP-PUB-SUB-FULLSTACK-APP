package com.jjra.chatbackend.controller;

import com.jjra.chatbackend.model.Message;
import com.jjra.chatbackend.repository.MessageRepository;
import com.jjra.chatbackend.service.MessageService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:3000")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    MessageRepository mesageRepository;
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = new ArrayList<>(mesageRepository.getMessages().values());
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @PostMapping("/message")
    public ResponseEntity<Message> postMessage(@RequestBody Message message) {
        logger.info("Creating new message!");
        Message publishedMessage = messageService.publishMessageToTopic(message);
        if(StringUtils.isNotEmpty(publishedMessage.getId())) {
            logger.info("Message with id = {} was created", publishedMessage.getId());
            return new ResponseEntity<>(publishedMessage, HttpStatus.OK);
        } else {
            logger.info("The message = {} was not published", message.getText());
            return new ResponseEntity<>(publishedMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
