package com.github.malkomich.event.stream.common;

public enum EventTopic {
    SCRAPPING("topic.scrapping"),
    ANALYZE("topic.analyze"),
    SIMULATOR("topic.simulator");

    private String topic;

    EventTopic(final String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }
}
