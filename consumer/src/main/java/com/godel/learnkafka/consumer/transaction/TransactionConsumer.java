package com.godel.learnkafka.consumer.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.godel.learnkafka.consumer.topic.Topic.TopicNames.TRANSACTION;

@Service
@RequiredArgsConstructor
public class TransactionConsumer {

    private final TransactionService service;

    @KafkaListener(
            topics = TRANSACTION,
            groupId = "transaction",
            containerFactory = "transactionListenerContainerFactory")
    public void listen(final Transaction transaction) {
        service.add(transaction);
    }

}
