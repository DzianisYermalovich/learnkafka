package com.godel.learnkafka.consumer.consumer;

import com.godel.learnkafka.consumer.client.Client;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

import static java.util.Map.of;
import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;

@Configuration
public class ListenerContainerFactoryConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServersConfig;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Client> clientListenerContainerFactory() {
        final var consumerFactory = new DefaultKafkaConsumerFactory<>(
                getCommonProps(),
                new StringDeserializer(),
                new JsonDeserializer<>(Client.class)
        );
        return getListenerContainerFactory(consumerFactory);
    }

    private Map<String, Object> getCommonProps() {
        return of(
                BOOTSTRAP_SERVERS_CONFIG, bootstrapServersConfig
        );
    }

    private <K, V> ConcurrentKafkaListenerContainerFactory<K, V> getListenerContainerFactory(
            ConsumerFactory<K, V> consumerFactory
    ) {
        final var listenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<K, V>();
        listenerContainerFactory.setConsumerFactory(consumerFactory);
        return listenerContainerFactory;
    }

}
