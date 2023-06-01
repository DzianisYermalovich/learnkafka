package com.godel.learnkafka.producer.client;

import com.godel.learnkafka.producer.base.BaseIntegrationTest;
import org.apache.kafka.clients.consumer.Consumer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

import static com.godel.learnkafka.producer.consumer.TestConsumerFactory.createConsumerForTopic;
import static com.godel.learnkafka.producer.consumer.TestConsumerUtils.getRecord;
import static com.godel.learnkafka.producer.topic.Topic.CLIENT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientIntegrationTest extends BaseIntegrationTest {

    private static final Long CLIENT_ID = 1L;
    private static final String EMAIL = "some@godeltech.com";

    private static Consumer<Long, Client> consumer;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeAll
    static void setup(@Autowired final EmbeddedKafkaBroker embeddedKafka) {
        consumer = createConsumerForTopic(embeddedKafka, CLIENT, Client.class);
    }

    @Test
    void shouldAddClientDataIntoKafka() {
        final var givenClient = new Client(CLIENT_ID, EMAIL);

        testRestTemplate.postForEntity("/clients", givenClient, Void.class)
                .getStatusCode()
                .is2xxSuccessful();

        final var actualClientRecord = getRecord(consumer);
        assertEquals(givenClient, actualClientRecord.value());
        assertEquals(CLIENT_ID, actualClientRecord.key());
    }

}
