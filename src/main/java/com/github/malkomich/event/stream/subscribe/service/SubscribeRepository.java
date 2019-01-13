package com.github.malkomich.event.stream.subscribe.service;

import com.github.malkomich.event.stream.subscribe.domain.SubscribeRequest;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

public interface SubscribeRepository {

    SubscribeRepository execute(final SubscribeRequest request, final Handler<AsyncResult<Void>> handler);

    void close(final Handler<AsyncResult<Void>> onClientStopped);
}
