package com.godel.learnkafka.producer.topic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;

@RequiredArgsConstructor
@Getter
public enum Topic {
    CLIENT(TopicNames.CLIENT);

    private final String name;

    @UtilityClass
    public static class TopicNames {
        public static final String CLIENT = "consumer";
    }

}
