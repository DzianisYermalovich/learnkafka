package com.godel.learnkafka.consumer.base;

import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.testcontainers.utility.DockerImageName.parse;

@SpringBootTest
@Testcontainers
public abstract class BaseIntegrationTest {

    @Container
    public KafkaContainer kafka = new KafkaContainer(parse("confluentinc/cp-kafka:7.4.0"))
            .withKraft();

}
