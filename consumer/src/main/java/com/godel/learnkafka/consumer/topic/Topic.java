package com.godel.learnkafka.consumer.topic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;

@RequiredArgsConstructor
@Getter
public enum Topic {

    CLIENT(TopicNames.CLIENT),
    TRANSACTION(TopicNames.TRANSACTION);

    private final String name;

    @UtilityClass
    public static class TopicNames {
        public static final String CLIENT = "consumer";
        public static final String TRANSACTION = "transaction";
    }

}
