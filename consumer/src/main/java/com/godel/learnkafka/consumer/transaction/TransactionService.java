package com.godel.learnkafka.consumer.transaction;

import com.godel.learnkafka.consumer.client.ClientEntity;
import com.godel.learnkafka.consumer.client.ClientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Optional.of;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionMapper mapper;
    private final TransactionRepository repository;
    private final ClientService clientService;

    @Transactional
    public void add(final Transaction transaction) {
        final var total = transaction.quantity() * transaction.price();
        final var transactionEntity = mapper.toEntity(transaction, total);
        addClientToTransaction(transaction.clientId(), transactionEntity);
        repository.save(transactionEntity);
    }

    private void addClientToTransaction(final Long clientId, final TransactionEntity transactionEntity) {
        clientService.get(clientId)
                .or(() -> getTemplateClientEntity(clientId))
                .ifPresent(transactionEntity::setClient);
    }

    private Optional<ClientEntity> getTemplateClientEntity(final Long clientId) {
        final var templateClientEntity = ClientEntity.builder()
                .id(clientId)
                .template(true)
                .build();
        return of(templateClientEntity);
    }

}
