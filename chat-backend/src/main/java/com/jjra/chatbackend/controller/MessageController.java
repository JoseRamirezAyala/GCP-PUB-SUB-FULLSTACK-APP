package com.jjra.chatbackend.controller;

import com.jjra.chatbackend.model.Message;
import com.jjra.chatbackend.service.MessageService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MessageController {

    @Autowired
    private MessageService messageService;
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);


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
