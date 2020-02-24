package com.steer.concurrent.future;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public interface IFuture<V> extends Future<V> {
    boolean isSuccess();

    /**
     * 不管Future是否isDone(),立刻返回结果
     * @return
     */
    V getNow();
    Throwable cause();
    boolean isCancellable();
    IFuture<V> await() throws InterruptedException;
    boolean await(long timeoutMillis) throws InterruptedException;
    boolean await(long timeout, TimeUnit timeUnit) throws InterruptedException;
    /**
     * 等待future完成，不响应中断
     * @return
     */
    IFuture<V> awaitUninterruptibly();
    boolean awaitUninterruptibly(long timeoutMillis);
    boolean awaitUninterruptibly(long timeout, TimeUnit timeUnit);
    IFuture<V> addListener(IFutureListener<V> l);
    IFuture<V> removeListener(IFutureListener<V> l);
}
