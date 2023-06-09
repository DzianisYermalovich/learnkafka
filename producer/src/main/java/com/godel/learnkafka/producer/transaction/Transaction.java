package com.godel.learnkafka.producer.transaction;

import java.time.LocalDateTime;

public record Transaction(
        String bank,
        Long clientId,
        OrderType orderType,
        Integer quantity,
        Double price,
        LocalDateTime createdAt
) {
}
