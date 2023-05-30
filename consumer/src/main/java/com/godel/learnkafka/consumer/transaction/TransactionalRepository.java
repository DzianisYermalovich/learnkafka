package com.godel.learnkafka.consumer.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionalRepository extends JpaRepository<TransactionEntity, Long> {
}
