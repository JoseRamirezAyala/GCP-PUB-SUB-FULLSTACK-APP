package com.jjra.chatbackend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.jjra.chatbackend.configuration.GCPConfig;
import com.jjra.chatbackend.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private GCPConfig gcpPublisherConfig;

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);


    @Override
    public Message publishMessageToTopic(Message message) {
        try {
            logger.info("publishing message={} to topic= {} on project={}",
                    message.getText(), gcpPublisherConfig.getTopicId(), gcpPublisherConfig.getProjectId());
            String text = message.getText();
            ByteString data = ByteString.copyFromUtf8(text);
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

            // Once published, returns a server-assigned message id (unique within the topic)
            Publisher publisher = gcpPublisherConfig.getPublisher();
            ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);
            String messageId = messageIdFuture.get();
            message.setId(messageId);
        } catch (Exception ex) {
            logger.error("MessageServiceImpl :: publishMessageToTopic error when publishing message to topic= "
                    + gcpPublisherConfig.getTopicId()
            + " error= "+ ex);
        }
        return message;
    }
}
