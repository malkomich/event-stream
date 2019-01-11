package com.github.malkomich.event.stream.publish.service;

import com.github.malkomich.event.stream.publish.domain.PublishRequest;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

public interface PublishRepository {

    PublishRepository execute(final PublishRequest request, final Handler<AsyncResult<Void>> handler);

    void close();
}
