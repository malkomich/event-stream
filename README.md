# Event Stream

[![Build Status](https://travis-ci.org/malkomich/event-stream.svg?branch=master)](https://travis-ci.org/malkomich/event-stream)


## Overview

Library to abstract publish and subscribe implementation in an event-based arquitecture.


## Requisites
* Java 8+
* Maven 3+


## Install

**Gradle**

```
    dependencies {
        compile 'com.github.malkomich:event-stream:1.0.2'
    }
```

**Maven**

```
    <dependency>
        <groupId>com.github.malkomich</groupId>
        <artifactId>event-stream</artifactId>
        <version>1.0.2</version>
    </dependency>
```


## Usage

##### INITIALIZATION:
```java
final KafkaConfig config = KafkaConfig.builder()
    .server("localhost:9092")
    .groupId("groupId")
    .build();

final EventService eventService = EventComponent.builder()
    .vertx(vertx)
    .kafkaConfig(config)
    .build();
```
##### PUBLISH:
```java
final EventTopic topic = BetchainTopic.QUOTE; // You can build your own EventTopic implementation

final PublishRequest request = PublishRequest.builder()
    .topic(topic)
    .message(data.toJson())
    .build();

eventService.publish(request, onEventPublished -> {
    if (onEventPublished.succeeded()) {
        future.complete();
    } else {
        future.fail(onEventPublished.cause());
    }
});
```

##### SUBSCRIBE:
```java
final EventTopic topic = BetchainTopic.QUOTE; // You can build your own EventTopic implementation

final SubscribeRequest request = SubscribeRequest.builder()
    .topic(topic)
    .handler(onEventReceived ->
            scrappingRepository.execute(onEventReceived.value(), Future.future()))
    .build();

eventService.subscribe(request, onSubscribed -> {
    if (onSubscribed.succeeded()) {
        handler.handle(Future.succeededFuture());
    } else {
        handler.handle(Future.failedFuture(onSubscribed.cause()));
    }
});
```


## License

[Apache License](http://www.apache.org/licenses/LICENSE-2.0.txt)

