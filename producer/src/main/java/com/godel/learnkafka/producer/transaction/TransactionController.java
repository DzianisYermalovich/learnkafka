package com.godel.learnkafka.producer.transaction;

import com.godel.learnkafka.producer.producer.Producer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.godel.learnkafka.producer.topic.Topic.TRANSACTION;
import static org.springframework.http.HttpStatus.ACCEPTED;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final Producer producer;

    @PostMapping
    @ResponseStatus(ACCEPTED)
    public void addTransactionData(@RequestBody Transaction transaction) {
        producer.send(TRANSACTION, transaction.clientId(), transaction);
    }

}
