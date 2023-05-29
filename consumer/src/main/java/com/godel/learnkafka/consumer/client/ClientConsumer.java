package com.godel.learnkafka.consumer.client;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.godel.learnkafka.consumer.topic.Topic.TopicNames.CLIENT;

@Service
@RequiredArgsConstructor
public class ClientConsumer {

    private final ClientService service;

    @KafkaListener(topics = CLIENT, groupId = "client", containerFactory = "clientListenerContainerFactory")
    public void listen(final Client client) {
        service.add(client);
    }

}
