package com.godel.learnkafka.consumer.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientMapper mapper;
    private final ClientRepository repository;

    public void add(final Client client) {
        final var clientEntity = mapper.toEntity(client);
        repository.save(clientEntity);
    }

    public Optional<ClientEntity> get(final Long id) {
        return repository.findById(id);
    }

}
