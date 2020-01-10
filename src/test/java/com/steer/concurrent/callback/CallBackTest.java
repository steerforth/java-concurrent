package com.steer.concurrent.callback;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.steer.concurrent.threadpool.MyRejectedExecutionHandler;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CallBackTest {
    Logger LOGGER = LoggerFactory.getLogger(CallBackTest.class);

    @Test
    public void test() throws IOException {
        ExecutorService pool = new ThreadPoolExecutor(40,100,0L, TimeUnit.SECONDS,new LinkedBlockingQueue<>(100),new ThreadFactoryBuilder().setNameFormat("mythread-%d").build(),new MyRejectedExecutionHandler());
        for (int i = 0; i <40; i++) {
            IRunnable runnable = new IRunnable();
            pool.execute(runnable);
        }

        LOGGER.info("任务提交完毕");

        //阻塞主线程
        System.in.read();
    }
}
