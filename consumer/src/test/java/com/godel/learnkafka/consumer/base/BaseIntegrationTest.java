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
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.testcontainers.utility.DockerImageName.parse;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
public abstract class BaseIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    static final KafkaContainer KAFKA_CONTAINER;
    static final PostgreSQLContainer<?> POSTGRE_CONTAINER;

    static {
        KAFKA_CONTAINER = new KafkaContainer(parse("confluentinc/cp-kafka:6.2.1"));
        KAFKA_CONTAINER.start();
        POSTGRE_CONTAINER = new PostgreSQLContainer<>(parse("postgres:15.3"));
        POSTGRE_CONTAINER.start();
    }


    @DynamicPropertySource
    static void setupProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);
        registry.add("spring.datasource.url", POSTGRE_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRE_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRE_CONTAINER::getPassword);
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
