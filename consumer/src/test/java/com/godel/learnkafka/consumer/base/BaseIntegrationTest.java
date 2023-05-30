package com.godel.learnkafka.consumer.base;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.testcontainers.utility.DockerImageName.parse;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
public abstract class BaseIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Container
    static KafkaContainer kafka = new KafkaContainer(parse("confluentinc/cp-kafka:6.2.1"));

    @Container
    static PostgreSQLContainer<?> postgre = new PostgreSQLContainer<>(parse("postgres:15.3"));


    @DynamicPropertySource
    static void setupProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("spring.datasource.url", postgre::getJdbcUrl);
        registry.add("spring.datasource.username", postgre::getUsername);
        registry.add("spring.datasource.password", postgre::getPassword);
    }

    @AfterEach
    void cleanupDb() {
        final var tableNames = List.of("transaction", "client");
        tableNames.forEach(this::cleanupTable);
    }

    private void cleanupTable(final String tableName) {
        jdbcTemplate.update("delete from " + tableName);
    }

}
