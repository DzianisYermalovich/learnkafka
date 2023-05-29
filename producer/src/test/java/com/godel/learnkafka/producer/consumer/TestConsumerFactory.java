package com.godel.learnkafka.producer.consumer;

import com.godel.learnkafka.producer.topic.Topic;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.ParseStringDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.springframework.kafka.support.serializer.JsonDeserializer.TRUSTED_PACKAGES;

public class TestConsumerFactory {

    public static <T> Consumer<Long, T> createConsumerForTopic(
            final EmbeddedKafkaBroker embeddedKafka,
            final Topic topic,
            final Class<T> clazz
    ) {
        final var consumer = createConsumer(embeddedKafka, clazz);
        embeddedKafka.consumeFromAnEmbeddedTopic(consumer, topic.getName());
        return consumer;
    }

    private static <T> Consumer<Long, T> createConsumer(final EmbeddedKafkaBroker embeddedKafka, final Class<T> clazz) {
        final var consumerProps = getConsumerProps(embeddedKafka, clazz.getSimpleName());
        final var consumerFactory = new DefaultKafkaConsumerFactory<>(
                consumerProps,
                new ParseStringDeserializer<>(Long::decode),
                new JsonDeserializer<>(clazz));
        return consumerFactory.createConsumer();
    }

    private static Map<String, Object> getConsumerProps(
            final EmbeddedKafkaBroker embeddedKafka,
            final String groupPostfix
    ) {
        final var group = "test-group-" + groupPostfix.toLowerCase();
        final var consumerProps = KafkaTestUtils.consumerProps(group, "true", embeddedKafka);
        consumerProps.put(AUTO_OFFSET_RESET_CONFIG, "earliest");
        return consumerProps;
    }

}
