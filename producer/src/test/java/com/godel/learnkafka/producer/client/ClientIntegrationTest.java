package com.godel.learnkafka.producer.client;

import org.apache.kafka.clients.consumer.Consumer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.Map;

import static com.godel.learnkafka.producer.topic.Topic.TopicNames.CLIENT;
import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.kafka.support.serializer.JsonDeserializer.TRUSTED_PACKAGES;
import static org.springframework.kafka.test.utils.KafkaTestUtils.getRecords;

@SpringBootTest(webEnvironment= RANDOM_PORT)
@EmbeddedKafka(
        topics = {CLIENT},
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

        final var actualClient = getActualClient();
        assertEquals(givenClient, actualClient);
    }

    private Client getActualClient() {
        final var consumer = getConsumer();
        final var records = getRecords(consumer);
        return records.iterator().next().value();
    }

    private Consumer<Object, Client> getConsumer() {
        final var consumerFactory = getConsumerFactory();
        final var consumer = consumerFactory.createConsumer();
        embeddedKafka.consumeFromAnEmbeddedTopic(consumer, CLIENT);
        return consumer;
    }

    private DefaultKafkaConsumerFactory<Object, Client> getConsumerFactory() {
        final var consumerProps = getConsumerProps();
        return  new DefaultKafkaConsumerFactory<>(consumerProps, null, new JsonDeserializer<>());
    }

    @NotNull
    private Map<String, Object> getConsumerProps() {
        final var consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafka);
        consumerProps.put(AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(TRUSTED_PACKAGES, "com.godel.learnkafka.*");
        return consumerProps;
    }

}
