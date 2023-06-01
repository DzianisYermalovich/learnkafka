package com.godel.learnkafka.producer.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import static org.springframework.kafka.test.utils.KafkaTestUtils.getRecords;

public class TestConsumerUtils {

    public static <T> ConsumerRecord<Long, T> getRecord(final Consumer<Long, T> consumer) {
        final var records = getRecords(consumer);
        return records.iterator().next();
    }

}
