package com.github.malkomich.event.stream.subscribe.service;

import com.github.malkomich.event.stream.amqp.KafkaConsumerFactory;
import com.github.malkomich.event.stream.subscribe.domain.SubscribeRequest;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KafkaSubscribeRepository implements SubscribeRepository {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(KafkaSubscribeRepository.class);

    private KafkaConsumerFactory consumerFactory;
    private List<KafkaConsumer> consumers;

    public KafkaSubscribeRepository(final KafkaConsumerFactory consumerFactory) {
        consumers = new ArrayList<>();
        this.consumerFactory = consumerFactory;
    }

    @Override
    public SubscribeRepository execute(final SubscribeRequest request, final Handler<AsyncResult<Void>> handler) {
        final KafkaConsumer<String, JsonObject> consumer = consumerFactory.createConsumer();
        consumer.handler(request.getHandler());
        consumer.subscribe(request.topicValue(), result -> {
            if (result.succeeded()) {
                log.info("Service successfully subscribed to topic {}", request.topicValue());
                handler.handle(Future.succeededFuture());
            } else {
                log.error("ERROR subscribing to topic {}", request.topicValue());
                handler.handle(Future.failedFuture(result.cause()));
            }
        });
        consumers.add(consumer);
        return this;
    }

    @Override
    public void close(final Handler<AsyncResult<Void>> handler) {
        final List<Future> closeFutures = consumers.stream()
            .map(this::closeFuture)
            .collect(Collectors.toList());

        CompositeFuture.all(closeFutures)
           .setHandler(onClientStopped -> {
               if (onClientStopped.succeeded()) {
                   log.info("Kafka consumer closed!");
                   handler.handle(Future.succeededFuture());
               }
           });
    }

    private Future closeFuture(final KafkaConsumer consumer) {
        final Future future = Future.future();
        consumer.close(onClosed -> future.complete());
        return future;
    }
}
