package com.jjra.chatbackend.configuration;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import com.jjra.chatbackend.model.Message;
import com.jjra.chatbackend.repository.MessageRepository;
import com.jjra.chatbackend.repository.MessageRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Configuration Class for a single gcp topic
 * @author Jose Ramirez
 */
@Configuration
public class GCPConfig {

    private static final Logger logger = LoggerFactory.getLogger(GCPConfig.class);

    @Autowired
    MessageRepository messageRepository;

    @Value("${gcp.project.id}")
    private String projectId;

    @Value("${gcp.message.topic.id}")
    private String topicId;

    @Value("messages-subscriber")
    private String subscriberId;

    private Publisher publisher = null;

    public Publisher getPublisher() {
        TopicName topicName = TopicName.of(projectId, topicId);
        try {
            // Create a publisher instance with default settings bound to the topic
            publisher = Publisher.newBuilder(topicName).build();
        } catch (Exception ex) {
            logger.error("GCPPublisherConfig :: getPublisher error building publisher. exception= " + ex);
        }
        return publisher;
    }




    public boolean shutDownPublisher() {
        try {
            publisher.shutdown();
            publisher = null;
            return true;
        } catch (Exception ex) {
            logger.error("GCPPublisherConfig :: shutDownPublisher error shuting down publisher. exception= " + ex);
            return false;
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void subscribeAsync() {
        ProjectSubscriptionName subscriptionName =
                ProjectSubscriptionName.of(projectId, subscriberId);

        // Instantiate an asynchronous message receiver.
        MessageReceiver receiver =
                (PubsubMessage message, AckReplyConsumer consumer) -> {
                    logger.info("Received message with Id= {} Data= {}",message.getMessageId(),message.getData().toStringUtf8());
                    Message newMessage = new Message(message.getMessageId(),message.getData().toStringUtf8());
                    messageRepository.saveMessage(newMessage);
                    consumer.ack();
                };

        Subscriber subscriber = null;
        try {
            subscriber = Subscriber.newBuilder(subscriptionName, receiver).build();
            // Start the subscriber.
            subscriber.startAsync().awaitRunning();
            logger.info("Listening for messages on {}:\n", subscriptionName.toString());
        } catch (Exception ex) {
            // Shut down the subscriber after 30s. Stop receiving messages.
            subscriber.stopAsync();
        }
    }

    public String getProjectId() {
        return projectId;
    }

    public String getTopicId() {
        return topicId;
    }

    public String getSubscriberId() {
        return subscriberId;
    }
}
