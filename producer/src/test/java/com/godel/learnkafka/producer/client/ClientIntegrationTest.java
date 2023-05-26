package com.godel.learnkafka.producer.client;

import com.godel.learnkafka.producer.topic.Topic;
import org.apache.kafka.clients.consumer.Consumer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;

import static com.godel.learnkafka.producer.consumer.TestConsumerFactory.createConsumerForTopic;
import static com.godel.learnkafka.producer.consumer.TestConsumerUtils.getRecord;
import static com.godel.learnkafka.producer.topic.Topic.CLIENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@EmbeddedKafka(
        topics = {Topic.TopicNames.CLIENT},
        bootstrapServersProperty = "spring.kafka.bootstrap-servers")
class ClientIntegrationTest {

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
        final var actualClient = actualClientRecord.value();
        assertEquals(givenClient, actualClient);
        assertEquals(CLIENT_ID, actualClientRecord.key());
    }

}
