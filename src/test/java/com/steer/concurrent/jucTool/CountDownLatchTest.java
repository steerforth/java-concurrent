package com.steer.concurrent.jucTool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 门栓
 */
public class CountDownLatchTest {
    private Logger LOGGER = LoggerFactory.getLogger(CountDownLatchTest.class);

    private final CountDownLatch latch = new CountDownLatch(1);
    @Test
    public void test() throws IOException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(3,3,0, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>(1),new ThreadFactoryBuilder().setNameFormat(new StringBuilder().append("test-").append("-%d").toString()).build());

        executor.execute(()->{
            LOGGER.info("开始执行第1个任务");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOGGER.info("第1个任务执行完毕，释放锁");
            latch.countDown();
        });

        executor.execute(()->{
            LOGGER.info("等待执行第2个任务");
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOGGER.info("开始执行第2个任务");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOGGER.info("第2个任务执行完毕");

        });

        executor.execute(()->{
            LOGGER.info("等待执行第3个任务");
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOGGER.info("开始执行第3个任务");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOGGER.info("第3个任务执行完毕");

        });

        executor.shutdown();
        System.in.read();
    }
}
