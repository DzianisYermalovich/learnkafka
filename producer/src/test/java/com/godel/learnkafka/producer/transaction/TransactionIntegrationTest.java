package com.godel.learnkafka.producer.transaction;

import com.godel.learnkafka.producer.base.BaseIntegrationTest;
import org.apache.kafka.clients.consumer.Consumer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

import java.time.LocalDateTime;

import static com.godel.learnkafka.producer.consumer.TestConsumerFactory.createConsumerForTopic;
import static com.godel.learnkafka.producer.consumer.TestConsumerUtils.getRecord;
import static com.godel.learnkafka.producer.topic.Topic.TRANSACTION;
import static com.godel.learnkafka.producer.transaction.OrderType.INCOME;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionIntegrationTest extends BaseIntegrationTest {

    final static String BANK = "TechnoBank";
    final static Long CLIENT_ID = 1L;
    final static OrderType ORDER_TYPE = INCOME;
    final static Integer QUANTITY = 3;
    final static Double PRICE = 9.99;
    final static LocalDateTime CREATED_AT = LocalDateTime.now();

    private static Consumer<Long, Transaction> consumer;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeAll
    static void setup(@Autowired final EmbeddedKafkaBroker embeddedKafka) {
        consumer = createConsumerForTopic(embeddedKafka, TRANSACTION, Transaction.class);
    }

    @Test
    void shouldAddTransactionDataIntoKafka() {
        final var givenTransaction = new Transaction(BANK, CLIENT_ID, ORDER_TYPE, QUANTITY, PRICE, CREATED_AT);

        testRestTemplate.postForEntity("/transactions", givenTransaction, Void.class)
                .getStatusCode()
                .is2xxSuccessful();

        final var actualTransactionRecord = getRecord(consumer);
        assertEquals(givenTransaction, actualTransactionRecord.value());
        assertEquals(CLIENT_ID, actualTransactionRecord.key());
    }

}
