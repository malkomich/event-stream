package com.github.malkomich.event.stream.topic.betchain;

import com.github.malkomich.event.stream.topic.EventTopic;

public enum BetchainTopic implements EventTopic {
    QUOTE("topic.quote"),
    FIFA_PLAYER("topic.fifa.player"),
    FIFA_TEAM("topic.fifa.team"),
    SIMULATOR("topic.simulator");

    private String topic;

    BetchainTopic(final String topic) {
        this.topic = topic;
    }

    @Override
    public String getTopic() {
        return topic;
    }
}
