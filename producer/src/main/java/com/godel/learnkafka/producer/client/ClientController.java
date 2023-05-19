package com.godel.learnkafka.producer.client;

import com.godel.learnkafka.producer.producer.Producer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.godel.learnkafka.producer.topic.Topic.CLIENT;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final Producer producer;

    @PostMapping
    public void addClientData(@RequestBody Client client) {
        producer.send(CLIENT, client);
    }

}
