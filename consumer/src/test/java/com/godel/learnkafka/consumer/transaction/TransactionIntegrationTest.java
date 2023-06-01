package com.godel.learnkafka.consumer.transaction;

import com.godel.learnkafka.consumer.base.BaseIntegrationTest;
import com.godel.learnkafka.consumer.client.ClientEntity;
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
    private TransactionRepository transactionRepository;

    @Test
    void shouldAddTransactionForNonExistedClient() {
        whenTransactionSent();

        thenTransactionAndTemplateClientAdded();
    }

    private void thenTransactionAndTemplateClientAdded() {
        final var actualTransaction = getActualTransaction();
        assertTransactionIsExpected(actualTransaction);
        assertClientIsTemplateClientForTransaction(actualTransaction.getClient());
    }

    private void assertClientIsTemplateClientForTransaction(ClientEntity client) {
        assertEquals(CLIENT_ID, client.getId());
        assertNull(client.getEmail());
        assertTrue(client.isTemplate());
    }

    @Test
    @Sql(statements = "insert into public.client (id, email, template) values (1, 'someemail@godeltech.com', false)")
    void shouldAddTransactionForExistingClient() {
        whenTransactionSent();

        thenTransactionAddedForExistingClient();
    }

    private void thenTransactionAddedForExistingClient() {
        final var actualTransaction = getActualTransaction();
        assertTransactionIsExpected(actualTransaction);
        assertClientIsExpected(actualTransaction.getClient());
    }

    private void assertClientIsExpected(ClientEntity client) {
        assertEquals(CLIENT_ID, client.getId());
        assertEquals("someemail@godeltech.com", client.getEmail());
        assertFalse(client.isTemplate());
    }

    private void whenTransactionSent() {
        final var givenTransaction = new Transaction(BANK, CLIENT_ID, ORDER_TYPE, QUANTITY, PRICE, CREATED_AT);

        testProducer.send(TRANSACTION, CLIENT_ID, givenTransaction);
        await().until(() -> transactionRepository.existsByClientId(CLIENT_ID));
    }

    private TransactionEntity getActualTransaction() {
        return transactionRepository.findByClientId(CLIENT_ID);
    }

    private void assertTransactionIsExpected(TransactionEntity transaction) {
        assertEquals(BANK, transaction.getBank());
        assertEquals(ORDER_TYPE, transaction.getOrderType());
        assertEquals(QUANTITY * PRICE, transaction.getTotal());
        assertEquals(CREATED_AT, transaction.getCreatedAt());
    }

}
