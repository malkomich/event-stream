package com.github.malkomich.event.stream.amqp;

import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.serialization.JsonObjectDeserializer;
import io.vertx.kafka.client.serialization.JsonObjectSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Optional;
import java.util.Properties;

public class KafkaConfig {
    private String server;
    private String defaultOffset;
    private Boolean autoCommit;
    private String groupId;
    private Integer acks;

    public KafkaConfig(final JsonObject json) {
        server = json.getString("server");
        defaultOffset = json.getString("defaultOffset");
        autoCommit = json.getBoolean("autoCommit");
        groupId = json.getString("groupId");
        acks = json.getInteger("acks");
    }

    @SuppressWarnings("checkstyle:parameternumber")
    private KafkaConfig(final String server,
                        final String defaultOffset,
                        final Boolean autoCommit,
                        final String groupId,
                        final Integer acks) {
        this.server = server;
        this.defaultOffset = defaultOffset;
        this.autoCommit = autoCommit;
        this.groupId = groupId;
        this.acks = acks;
    }

    public static KafkaConfigBuilder builder() {
        return new KafkaConfigBuilder();
    }

    public Properties toProperties() {
        final Properties properties = new Properties();
        putIfNotNull(properties, ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        putIfNotNull(properties, ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, defaultOffset);
        putIfNotNull(properties, ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, String.valueOf(autoCommit));
        putIfNotNull(properties, ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        putIfNotNull(properties, ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonObjectDeserializer.class);
        putIfNotNull(properties, ConsumerConfig.GROUP_ID_CONFIG, groupId);

        putIfNotNull(properties, ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        putIfNotNull(properties, ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonObjectSerializer.class);
        putIfNotNull(properties, ProducerConfig.ACKS_CONFIG, String.valueOf(acks));

        return properties;
    }

    private void putIfNotNull(final Properties properties, final String key, final Object value) {
        Optional.ofNullable(value)
            .ifPresent(notNullValue -> properties.put(key, notNullValue));
    }

    public KafkaConfigBuilder toBuilder() {
        return new KafkaConfigBuilder()
                .server(this.server)
                .defaultOffset(this.defaultOffset)
                .autoCommit(this.autoCommit)
                .groupId(this.groupId)
                .acks(this.acks);
    }

    public static class KafkaConfigBuilder {
        private String server;
        private String defaultOffset;
        private Boolean autoCommit;
        private String groupId;
        private Integer acks;

        KafkaConfigBuilder() {
        }

        public KafkaConfig.KafkaConfigBuilder server(final String server) {
            this.server = server;
            return this;
        }

        public KafkaConfig.KafkaConfigBuilder defaultOffset(final String defaultOffset) {
            this.defaultOffset = defaultOffset;
            return this;
        }

        public KafkaConfig.KafkaConfigBuilder autoCommit(final Boolean autoCommit) {
            this.autoCommit = autoCommit;
            return this;
        }

        public KafkaConfig.KafkaConfigBuilder groupId(final String groupId) {
            this.groupId = groupId;
            return this;
        }

        public KafkaConfig.KafkaConfigBuilder acks(final Integer acks) {
            this.acks = acks;
            return this;
        }

        public KafkaConfig build() {
            return new KafkaConfig(server, defaultOffset, autoCommit, groupId, acks);
        }

        public String toString() {
            return "KafkaConfig.KafkaConfigBuilder(server=" + this.server
                    + ", defaultOffset=" + this.defaultOffset
                    + ", autoCommit=" + this.autoCommit
                    + ", groupId=" + this.groupId
                    + ", acks=" + this.acks + ")";
        }
    }
}
