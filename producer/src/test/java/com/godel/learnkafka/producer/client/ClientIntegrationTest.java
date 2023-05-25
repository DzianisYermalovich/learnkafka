package com.godel.learnkafka.producer.client;

import com.godel.learnkafka.producer.topic.Topic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;

import static com.godel.learnkafka.producer.consumer.TestConsumerFactory.createConsumerForTopic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.kafka.test.utils.KafkaTestUtils.getRecords;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@EmbeddedKafka(
        topics = {Topic.TopicNames.CLIENT},
        bootstrapServersProperty = "spring.kafka.bootstrap-servers")
class ClientIntegrationTest {

    private static final Long CLIENT_ID = 1L;
    private static final String EMAIL = "some@godeltech.com";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @Test
    void shouldAddClientDataIntoKafka() {
        final var givenClient = new Client(CLIENT_ID, EMAIL);

        testRestTemplate.postForEntity("/clients", givenClient, Void.class)
                .getStatusCode()
                .is2xxSuccessful();

        final var actualClientRecord = getRecord();
        final var actualClient = actualClientRecord.value();
        assertEquals(givenClient, actualClient);
        assertEquals(CLIENT_ID, actualClientRecord.key());
    }

    @Test
    @Disabled
    void duplicate() {
        shouldAddClientDataIntoKafka();
    }

    private ConsumerRecord<Long, Client> getRecord() {
        final var consumer = createConsumerForTopic(embeddedKafka, Topic.CLIENT, Client.class);
        final var records = getRecords(consumer);
        return records.iterator().next();
    }

}
