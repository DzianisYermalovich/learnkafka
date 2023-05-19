package com.godel.learnkafka.producer.topic;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin.NewTopics;

import java.util.EnumSet;

@Configuration
public class TopicConfiguration {

    @Bean
    public NewTopics topics() {
        final var topics = getTopics();
        return new NewTopics(topics);
    }

    private NewTopic[] getTopics() {
        return EnumSet.allOf(Topic.class)
                .stream()
                .map(topic -> new NewTopic(topic.getName(), 1, (short) 1))
                .toArray(NewTopic[]::new);
    }
}
