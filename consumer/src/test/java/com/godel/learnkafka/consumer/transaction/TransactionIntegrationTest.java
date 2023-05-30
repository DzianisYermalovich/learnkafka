package com.godel.learnkafka.consumer.transaction;

import com.godel.learnkafka.consumer.base.BaseIntegrationTest;
import com.godel.learnkafka.consumer.producer.TestProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static com.godel.learnkafka.consumer.topic.Topic.TRANSACTION;
import static com.godel.learnkafka.consumer.transaction.OrderType.INCOME;
import static org.junit.jupiter.api.Assertions.*;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

public class TransactionIntegrationTest extends BaseIntegrationTest {

    final static String BANK = "TechnoBank";
    final static Long CLIENT_ID = 1L;
    final static OrderType ORDER_TYPE = INCOME;
    final static Integer QUANTITY = 3;
    final static Double PRICE = 9.99;
    final static LocalDateTime CREATED_AT = LocalDateTime.of(2023, 5, 30, 16, 36, 7);

    @Autowired
    private TestProducer testProducer;

    @Autowired
    private TransactionalRepository transactionalRepository;

    @Test
    void shouldAddTransactionWithoutClient() {
        final var givenTransaction = new Transaction(BANK, CLIENT_ID, ORDER_TYPE, QUANTITY, PRICE, CREATED_AT);

        testProducer.send(TRANSACTION, CLIENT_ID, givenTransaction);
        await().until(() -> transactionalRepository.existsByClientId(CLIENT_ID));

        final var actualTransaction = transactionalRepository.findByClientId(CLIENT_ID);
        assertEquals(BANK, actualTransaction.getBank());
        assertEquals(ORDER_TYPE, actualTransaction.getOrderType());
        assertEquals(QUANTITY * PRICE, actualTransaction.getTotal());
        assertEquals(CREATED_AT, actualTransaction.getCreatedAt());
        final var actualClient = actualTransaction.getClient();
        assertEquals(CLIENT_ID, actualClient.getId());
        assertNull(actualClient.getEmail());
        assertTrue(actualClient.isTemplate());
    }

    @Test
    @Sql(statements = "insert into public.client (id, email, template) values (1, 'someemail@godeltech.com', false)")
    void shouldAddTransactionWithExistingClient() {
        final var givenTransaction = new Transaction(BANK, CLIENT_ID, ORDER_TYPE, QUANTITY, PRICE, CREATED_AT);

        testProducer.send(TRANSACTION, CLIENT_ID, givenTransaction);
        await().until(() -> transactionalRepository.existsByClientId(CLIENT_ID));

        final var actualTransaction = transactionalRepository.findByClientId(CLIENT_ID);
        assertEquals(BANK, actualTransaction.getBank());
        assertEquals(ORDER_TYPE, actualTransaction.getOrderType());
        assertEquals(QUANTITY * PRICE, actualTransaction.getTotal());
        assertEquals(CREATED_AT, actualTransaction.getCreatedAt());
        final var actualClient = actualTransaction.getClient();
        assertEquals(CLIENT_ID, actualClient.getId());
        assertEquals("someemail@godeltech.com", actualClient.getEmail());
        assertFalse(actualClient.isTemplate());
    }

}
