package com.github.malkomich.event.stream.common;

import io.vertx.core.json.JsonObject;
import lombok.Getter;

@Getter
public abstract class EventRequest {

    private EventTopic topic;

    public EventRequest(final EventTopic topic) {
        this.topic = topic;
    }

    public String topicValue() {
        return topic.getTopic();
    }

    public JsonObject toJson() {
        return JsonObject.mapFrom(this);
    }
}
