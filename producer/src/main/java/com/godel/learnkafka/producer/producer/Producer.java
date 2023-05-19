package com.godel.learnkafka.producer.producer;

import com.godel.learnkafka.producer.client.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.godel.learnkafka.producer.topic.Topic.TopicNames.CLIENT;

@Service
@RequiredArgsConstructor
public class Producer {

    private final KafkaTemplate<Object, Object> kafkaTemplate;

    public void send(final Client client) {
        kafkaTemplate.send(CLIENT, client);
    }
}
