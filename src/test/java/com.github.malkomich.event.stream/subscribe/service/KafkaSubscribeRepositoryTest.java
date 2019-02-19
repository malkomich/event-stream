package com.github.malkomich.event.stream.subscribe.service;

import com.github.malkomich.event.stream.amqp.KafkaConsumerFactory;
import com.github.malkomich.event.stream.subscribe.domain.SubscribeRequest;
import com.github.malkomich.event.stream.topic.EventTopic;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import io.vertx.kafka.client.consumer.KafkaConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class KafkaSubscribeRepositoryTest {

    @Mock
    private KafkaConsumerFactory kafkaConsumerFactory;
    @Mock
    private KafkaConsumer<String, JsonObject> kafkaConsumer;

    private KafkaSubscribeRepository kafkaSubscribeRepository;

    @BeforeEach
    void setUp() {
        initMocks(this);
        kafkaSubscribeRepository = new KafkaSubscribeRepository(kafkaConsumerFactory);

        when(kafkaConsumerFactory.createConsumer()).thenReturn(kafkaConsumer);
    }

    @Test
    void executeWithoutRequest() {
        final Executable nullPointerExecutable = () -> kafkaSubscribeRepository.execute(null, handler -> {
            // Hello World!
        });
        assertThrows(NullPointerException.class, nullPointerExecutable);
    }

    @Test
    void executeWithoutHandler() {
        final SubscribeRequest request = subscribeRequestFixture();
        kafkaSubscribeRepository.execute(request, null);
    }

    @Test
    void executeWithoutTopic() {
        final SubscribeRequest request = SubscribeRequest.builder().build();
        when(kafkaConsumer.subscribe(org.mockito.Mockito.<String>isNull(), any(Handler.class)))
                .thenThrow(IllegalArgumentException.class);
        final Executable illegalExecution = () -> kafkaSubscribeRepository.execute(request, handler -> {
            // Hello World!
        });
        assertThrows(IllegalArgumentException.class, illegalExecution);
    }

    @Test
    void closeWithoutHandler() {
        final Executable illegalExecution = () -> kafkaSubscribeRepository.close(null);
        assertThrows(NullPointerException.class, illegalExecution);
    }

    private SubscribeRequest subscribeRequestFixture() {
        return SubscribeRequest.builder()
                .topic(TestingTopic.TEST)
                .handler(KafkaConsumerRecord::value)
                .build();
    }

    enum TestingTopic implements EventTopic {
        TEST("test");

        private String topic;

        TestingTopic(final String topic) {
            this.topic = topic;
        }

        @Override
        public String getTopic() {
            return topic;
        }
    }
}
