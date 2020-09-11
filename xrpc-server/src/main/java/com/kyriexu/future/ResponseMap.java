package com.kyriexu.future;

import com.kyriexu.enity.Response;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author KyrieXu
 * @since 2020/9/3 8:39
 **/
public class ResponseMap {
    private static final Map<String, CompletableFuture<Response>> FUTURE_MAP = new ConcurrentHashMap<>();

    public void put(String requestId, CompletableFuture<Response> future) {
        FUTURE_MAP.put(requestId, future);
    }

    public void complete(Response response) {
        if(response.getId() == null)
            return;
        CompletableFuture<Response> future = FUTURE_MAP.remove(response.getId());
        if (null != future) {
            future.complete(response);
        } else {
            throw new IllegalStateException();
        }
    }
}
