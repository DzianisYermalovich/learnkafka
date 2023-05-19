package com.godel.learnkafka.producer.transaction;

import com.godel.learnkafka.producer.producer.Producer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.godel.learnkafka.producer.topic.Topic.TRANSACTION;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final Producer producer;

    @PostMapping
    public void addTransactionData(@RequestBody Transaction transaction) {
        producer.send(TRANSACTION, transaction);
    }

}
