package com.jjra.chatbackend.configuration;

import com.google.cloud.pubsub.v1.Publisher;
import com.google.pubsub.v1.TopicName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Configuration Class for a single gcp topic
 * @author Jose Ramirez
 */
@Configuration
public class GCPPublisherConfig {

    private static final Logger logger = LoggerFactory.getLogger(GCPPublisherConfig.class);

    @Value("${gcp.project.id}")
    private String projectId;

    @Value("${gcp.message.topic.id}")
    private String topicId;

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

    public String getProjectId() {
        return projectId;
    }

    public String getTopicId() {
        return topicId;
    }
}
