package com.godel.learnkafka.consumer.transaction;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionalRepository extends JpaRepository<TransactionEntity, Long> {

    boolean existsByClientId(final Long clientId);

    @EntityGraph(attributePaths = "client")
    TransactionEntity findByClientId(final Long clientId);

}
