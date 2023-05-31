package com.godel.learnkafka.consumer.producer;

import com.godel.learnkafka.consumer.topic.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TestProducer {

    @Autowired
    private KafkaTemplate<Object, Object> kafkaTemplate;

    public void send(final Topic topic, final Object key, final Object value) {
        kafkaTemplate.send(topic.getName(), key, value);
    }

}
