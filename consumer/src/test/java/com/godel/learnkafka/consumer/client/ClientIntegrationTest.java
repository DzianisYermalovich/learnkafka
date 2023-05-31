package com.godel.learnkafka.consumer.client;

import com.godel.learnkafka.consumer.base.BaseIntegrationTest;
import com.godel.learnkafka.consumer.producer.TestProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.godel.learnkafka.consumer.topic.Topic.CLIENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

class ClientIntegrationTest extends BaseIntegrationTest {

    private static final Long CLIENT_ID = 1L;
    private static final String EMAIL = "some@godeltech.com";

    @Autowired
    private TestProducer testProducer;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void shouldAddClient() {
        final var givenClient = new Client(CLIENT_ID, EMAIL);

        testProducer.send(CLIENT, CLIENT_ID, givenClient);
        await().until(() -> clientRepository.existsById(CLIENT_ID));

        final var actualClientEntity = clientRepository.findById(CLIENT_ID)
                .orElseThrow();
        assertEquals(CLIENT_ID, actualClientEntity.getId());
        assertEquals(EMAIL, actualClientEntity.getEmail());
        assertFalse(actualClientEntity.isTemplate());
    }

}
