package com.godel.learnkafka.consumer.transaction;

import com.godel.learnkafka.consumer.client.ClientEntity;
import com.godel.learnkafka.consumer.client.ClientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static java.util.Optional.of;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionMapper mapper;
    private final TransactionalRepository repository;
    private final ClientService clientService;

    @Transactional
    public void add(final Transaction transaction) {
        final var total = transaction.quantity() * transaction.price();
        final var transactionEntity = mapper.toEntity(transaction, total);
        final var clientId = transaction.clientId();
        clientService.get(clientId)
                .or(() -> of(getTemplateClientEntity(clientId)))
                .ifPresent(transactionEntity::setClient);
        repository.save(transactionEntity);
    }

    private ClientEntity getTemplateClientEntity(Long clientId) {
        return ClientEntity.builder()
                .id(clientId)
                .template(true)
                .build();
    }

}
