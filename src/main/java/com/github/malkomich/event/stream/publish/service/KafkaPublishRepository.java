package com.github.malkomich.event.stream.publish.service;

import com.github.malkomich.event.stream.publish.domain.PublishRequest;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import io.vertx.kafka.client.producer.RecordMetadata;
import org.slf4j.Logger;

public class KafkaPublishRepository implements PublishRepository {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(KafkaPublishRepository.class);

    private KafkaProducer<String, JsonObject> producer;

    public KafkaPublishRepository(final KafkaProducer<String, JsonObject> producer) {
        this.producer = producer;
    }

    @Override
    public PublishRepository execute(final PublishRequest request, final Handler<AsyncResult<Void>> handler) {
        final KafkaProducerRecord<String, JsonObject> record =
            KafkaProducerRecord.create(request.topicValue(), request.getMessage());

        producer.write(record, result -> writeHandler(handler, record, result));

        return this;
    }

    @Override
    public void close(final Handler<AsyncResult<Void>> onStopped) {
        producer.close(onClientStopped -> {
            log.info("Kafka consumer closed!");
            onStopped.handle(Future.succeededFuture());
        });
    }

    private void writeHandler(final Handler<AsyncResult<Void>> handler,
                              final KafkaProducerRecord<String, JsonObject> record,
                              final AsyncResult<RecordMetadata> result) {
        if (result.succeeded()) {
            final RecordMetadata recordMetadata = result.result();
            log.info("Message {} written on topic = {}, partition = {}, offset = {}", record.value(),
                    recordMetadata.getTopic(), recordMetadata.getPartition(), recordMetadata.getOffset());
            handler.handle(Future.succeededFuture());
            return;
        }
        handler.handle(Future.failedFuture(result.cause()));
    }
}
