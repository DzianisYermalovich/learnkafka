package com.godel.learnkafka.consumer.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientMapper mapper;
    private final ClientRepository repository;

    public void addClient(final Client client) {
        final var clientEntity = mapper.toEntity(client);
        repository.save(clientEntity);
    }

}
