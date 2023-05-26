package com.godel.learnkafka.producer.producer;

import com.godel.learnkafka.producer.topic.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Producer {

    private final KafkaTemplate<Object, Object> kafkaTemplate;

    public void send(final Topic topic, final Object key, final Object body) {
        kafkaTemplate.send(topic.getName(), key, body);
    }

}
