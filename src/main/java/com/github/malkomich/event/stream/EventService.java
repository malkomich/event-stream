package com.github.malkomich.event.stream;

import com.github.malkomich.event.stream.publish.domain.PublishRequest;
import com.github.malkomich.event.stream.subscribe.domain.SubscribeRequest;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

public interface EventService {

    void publish(final PublishRequest request, final Handler<AsyncResult<Void>> handler);

    void subscribe(final SubscribeRequest request, final Handler<AsyncResult<Void>> handler);

    void close();
}
