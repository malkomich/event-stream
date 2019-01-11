package com.github.malkomich.event.stream.amqp;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class KafkaConsumerFactory {

    private final Vertx vertx;
    private final KafkaConfig config;

    public KafkaConsumer<String, JsonObject> createConsumer() {
        return KafkaConsumer.create(vertx, config.toProperties());
    }
}
