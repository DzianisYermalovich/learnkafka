package com.godel.learnkafka.producer.base;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import static com.godel.learnkafka.producer.topic.Topic.TopicNames.CLIENT;
import static com.godel.learnkafka.producer.topic.Topic.TopicNames.TRANSACTION;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@EmbeddedKafka(
        topics = {CLIENT, TRANSACTION},
        bootstrapServersProperty = "spring.kafka.bootstrap-servers")
public abstract class BaseIntegrationTest {
}
