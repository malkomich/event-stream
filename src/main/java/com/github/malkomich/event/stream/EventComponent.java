package com.github.malkomich.event.stream;

import com.github.malkomich.event.stream.amqp.KafkaConfig;
import com.github.malkomich.event.stream.amqp.KafkaConsumerFactory;
import com.github.malkomich.event.stream.publish.domain.PublishRequest;
import com.github.malkomich.event.stream.publish.service.KafkaPublishRepository;
import com.github.malkomich.event.stream.publish.service.PublishRepository;
import com.github.malkomich.event.stream.subscribe.domain.SubscribeRequest;
import com.github.malkomich.event.stream.subscribe.service.KafkaSubscribeRepository;
import com.github.malkomich.event.stream.subscribe.service.SubscribeRepository;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.kafka.client.producer.KafkaProducer;

public class EventComponent implements EventService {

    private PublishRepository publishRepository;
    private SubscribeRepository subscribeRepository;

    private EventComponent(final PublishRepository publishRepository, final SubscribeRepository subscribeRepository) {
        this.publishRepository = publishRepository;
        this.subscribeRepository = subscribeRepository;
    }

    public static EventComponentBuilder builder() {
        return new EventComponentBuilder();
    }

    @Override
    public void publish(final PublishRequest request, final Handler<AsyncResult<Void>> handler) {
        publishRepository.execute(request, handler);
    }

    @Override
    public void subscribe(final SubscribeRequest request, final Handler<AsyncResult<Void>> handler) {
        subscribeRepository.execute(request, handler);
    }

    @Override
    public void close() {
        publishRepository.close();
        subscribeRepository.close();
    }

    public static class EventComponentBuilder {
        private Vertx vertx;
        private KafkaConfig kafkaConfig;

        EventComponentBuilder() {
        }

        public EventComponentBuilder vertx(final Vertx vertx) {
            this.vertx = vertx;
            return this;
        }

        public EventComponentBuilder kafkaConfig(final KafkaConfig kafkaConfig) {
            this.kafkaConfig = kafkaConfig;
            return this;
        }

        public EventComponent build() {
            final PublishRepository publishRepository =
                    new KafkaPublishRepository(KafkaProducer.create(vertx, kafkaConfig.toProperties()));
            final  SubscribeRepository subscribeRepository =
                    new KafkaSubscribeRepository(new KafkaConsumerFactory(vertx, kafkaConfig));
            return new EventComponent(publishRepository, subscribeRepository);
        }
    }
}
