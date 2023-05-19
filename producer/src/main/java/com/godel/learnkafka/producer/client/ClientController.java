package com.godel.learnkafka.producer.client;

import com.godel.learnkafka.producer.producer.Producer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final Producer producer;

    @PostMapping
    public void addClientData(@RequestBody Client client) {
        producer.send(client);
    }

}
