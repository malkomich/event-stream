package com.github.malkomich.event.stream.subscribe.domain;

import com.github.malkomich.event.stream.common.EventRequest;
import com.github.malkomich.event.stream.common.EventTopic;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.consumer.KafkaConsumerRecord;
import lombok.Getter;

@Getter
public class SubscribeRequest extends EventRequest {

    private Handler<KafkaConsumerRecord<String, JsonObject>> handler;

    SubscribeRequest(final EventTopic topic,
                     final Handler<KafkaConsumerRecord<String, JsonObject>> handler) {
        super(topic);
        this.handler = handler;
    }

    public static SubscribeRequestBuilder builder() {
        return new SubscribeRequestBuilder();
    }

    public static class SubscribeRequestBuilder {
        private EventTopic topic;
        private Handler<KafkaConsumerRecord<String, JsonObject>> handler;

        SubscribeRequestBuilder() {
        }

        public SubscribeRequestBuilder topic(final EventTopic topic) {
            this.topic = topic;
            return this;
        }

        public SubscribeRequestBuilder handler(final Handler<KafkaConsumerRecord<String, JsonObject>> handler) {
            this.handler = handler;
            return this;
        }

        public SubscribeRequest build() {
            return new SubscribeRequest(topic, handler);
        }

        public String toString() {
            return "SubscribeRequest.SubscribeRequestBuilder(topic=" + this.topic
                    + ", handler=" + this.handler + ")";
        }
    }
}
