package com.godel.learnkafka.producer.client;

import com.godel.learnkafka.producer.producer.Producer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.godel.learnkafka.producer.topic.Topic.CLIENT;
import static org.springframework.http.HttpStatus.ACCEPTED;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final Producer producer;

    @PostMapping
    @ResponseStatus(ACCEPTED)
    public void addClientData(@RequestBody final Client client) {
        producer.send(CLIENT, client.clientId(), client);
    }

}
