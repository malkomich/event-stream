package com.github.malkomich.event.stream.publish.service;

import com.github.malkomich.event.stream.publish.domain.PublishRequest;
import com.github.malkomich.event.stream.topic.EventTopic;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import mockit.MockUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.MockitoAnnotations.initMocks;

class KafkaPublishRepositoryTest {

    private static final JsonObject MESSAGE = new JsonObject().put("message", "message");

    @Mock
    private KafkaProducer<String, JsonObject> kafkaProducer;
    @Mock
    private KafkaProducerRecord<String, JsonObject> kafkaProducerRecord;

    private KafkaPublishRepository kafkaPublishRepository;

    @BeforeEach
    void setUp() {
        initMocks(this);
        kafkaPublishRepository = new KafkaPublishRepository(kafkaProducer);
    }

    @Test
    void executeWithoutRequest() {
        final Executable nullPointerExecutable = () -> kafkaPublishRepository.execute(null, handler -> {
            // Hello World!
        });
        assertThrows(NullPointerException.class, nullPointerExecutable);
    }

    @Test
    void executeWithoutHandler() {
        final PublishRequest request = publishRequestFixture();
        kafkaPublishRepository.execute(request, null);
    }

    private PublishRequest publishRequestFixture() {
        return PublishRequest.builder()
                .topic(TestingTopic.TEST)
                .message(MESSAGE)
                .build();
    }

    private void prepareMocks() {
        new MockUp<KafkaProducerRecord>() {
            @mockit.Mock
            public KafkaProducerRecord create() {
                return kafkaProducerRecord;
            }
        };
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
