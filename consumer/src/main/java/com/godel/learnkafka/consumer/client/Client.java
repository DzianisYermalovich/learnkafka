package com.godel.learnkafka.consumer.client;

public record Client(
        Long clientId,
        String email
) {
}
