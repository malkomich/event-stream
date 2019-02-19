package com.github.malkomich.event.stream.common;

import com.github.malkomich.event.stream.topic.EventTopic;
import io.vertx.core.json.JsonObject;

import java.util.Optional;

public abstract class EventRequest {

    private EventTopic topic;

    public EventRequest(final EventTopic topic) {
        this.topic = topic;
    }

    public String topicValue() {
        return Optional.ofNullable(topic)
                .map(EventTopic::getTopic)
                .orElse(null);
    }

    public JsonObject toJson() {
        return JsonObject.mapFrom(this);
    }

    public EventTopic getTopic() {
        return this.topic;
    }
}
