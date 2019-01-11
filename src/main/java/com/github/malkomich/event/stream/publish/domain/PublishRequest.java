package com.github.malkomich.event.stream.publish.domain;

import com.github.malkomich.event.stream.common.EventRequest;
import com.github.malkomich.event.stream.common.EventTopic;
import io.vertx.core.json.JsonObject;
import lombok.Getter;

@Getter
public class PublishRequest extends EventRequest {

    private JsonObject message;

    private PublishRequest(final EventTopic topic,
                           final JsonObject message) {
        super(topic);
        this.message = message;
    }

    public static PublishRequestBuilder builder() {
        return new PublishRequestBuilder();
    }

    public JsonObject getMessage() {
        return message;
    }

    public static class PublishRequestBuilder {
        private EventTopic topic;
        private JsonObject message;

        PublishRequestBuilder() {
        }

        public PublishRequest.PublishRequestBuilder topic(final EventTopic topic) {
            this.topic = topic;
            return this;
        }

        public PublishRequest.PublishRequestBuilder message(final JsonObject message) {
            this.message = message;
            return this;
        }

        public PublishRequest build() {
            return new PublishRequest(topic, message);
        }

        public String toString() {
            return "SubscribeRequest.SubscribeRequestBuilder(topic=" + this.topic
                    + ", message=" + this.message + ")";
        }
    }
}
