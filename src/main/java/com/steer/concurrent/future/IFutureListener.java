package com.steer.concurrent.future;

public interface IFutureListener<V> {
    void onComplete(IFuture<V> future);
}
