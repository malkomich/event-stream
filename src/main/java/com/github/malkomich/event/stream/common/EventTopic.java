package com.github.malkomich.event.stream.common;

import lombok.Getter;

import java.util.Arrays;

public enum EventTopic {
    SCRAPPING("topic.scrapping"),
    SIMULATOR("topic.simulator");

    @Getter
    private String topic;

    EventTopic(final String topic) {
        this.topic = topic;
    }

    public static EventTopic from(final String topic) {
        return Arrays.stream(EventTopic.values())
                .filter(eventTopic -> eventTopic.topic.toLowerCase().equals(topic.toLowerCase()))
                .findFirst()
                .orElse(null);
    }
}
