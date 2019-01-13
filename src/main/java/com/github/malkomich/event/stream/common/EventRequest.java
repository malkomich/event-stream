package com.github.malkomich.event.stream.common;

import io.vertx.core.json.JsonObject;

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

    public EventTopic getTopic() {
        return this.topic;
    }
}
