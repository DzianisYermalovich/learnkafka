package com.godel.learnkafka.producer;

import com.godel.learnkafka.producer.transaction.OrderType;
import com.godel.learnkafka.producer.transaction.Transaction;
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

import java.time.LocalDateTime;
import java.util.Map;

import static com.godel.learnkafka.producer.topic.Topic.TopicNames.TRANSACTION;
import static com.godel.learnkafka.producer.transaction.OrderType.INCOME;
import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.kafka.support.serializer.JsonDeserializer.TRUSTED_PACKAGES;
import static org.springframework.kafka.test.utils.KafkaTestUtils.getRecords;

@SpringBootTest(webEnvironment= RANDOM_PORT)
@EmbeddedKafka(
        topics = {TRANSACTION},
        bootstrapServersProperty = "spring.kafka.bootstrap-servers")
class TransactionIntegrationTest {

    final static String BANK = "TechnoBank";
    final static Long CLIENT_ID = 1L;
    final static OrderType ORDER_TYPE = INCOME;
    final static Integer QUANTITY = 3;
    final static Double PRICE = 9.99;
    final static LocalDateTime CREATED_AT = LocalDateTime.now();

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @Test
    void shouldAddTransactionDataIntoKafka() {
        final var givenTransaction = new Transaction(BANK, CLIENT_ID, ORDER_TYPE, QUANTITY, PRICE, CREATED_AT);

        testRestTemplate.postForEntity("/transactions", givenTransaction, Void.class)
                .getStatusCode()
                .is2xxSuccessful();

        final var actualTransaction = getActualTransaction();
        assertEquals(givenTransaction, actualTransaction);
    }

    private Transaction getActualTransaction() {
        final var consumer = getConsumer();
        final var records = getRecords(consumer);
        return records.iterator().next().value();
    }

    private Consumer<Object, Transaction> getConsumer() {
        final var consumerFactory = getConsumerFactory();
        final var consumer = consumerFactory.createConsumer();
        embeddedKafka.consumeFromAnEmbeddedTopic(consumer, TRANSACTION);
        return consumer;
    }

    private DefaultKafkaConsumerFactory<Object, Transaction> getConsumerFactory() {
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
