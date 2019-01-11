package com.github.malkomich.event.stream.amqp;

import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.serialization.JsonObjectDeserializer;
import io.vertx.kafka.client.serialization.JsonObjectSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Optional;
import java.util.Properties;

@Builder(toBuilder = true)
@AllArgsConstructor
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
        acks = json.getInteger("acks");
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
}
