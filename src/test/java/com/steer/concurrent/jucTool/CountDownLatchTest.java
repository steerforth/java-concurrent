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
 *  缺点:计数器只能使用一次
 * 可以让线程顺序执行任务
 */
public class CountDownLatchTest {
    private Logger LOGGER = LoggerFactory.getLogger(CountDownLatchTest.class);

    private final CountDownLatch latch = new CountDownLatch(1);

    /**
     * 第1个任务先执行完成，然后2，3任务是并发执行的
     * @throws IOException
     */
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
            latch.countDown();//计数器归零时，所有await等待的线程会被唤醒
        });

        executor.execute(()->{
            LOGGER.info("等待执行第2个任务");
            try {
                latch.await();//
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOGGER.info("开始执行第2个任务");//2,3任务是并发执行的
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
                latch.await();//
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOGGER.info("开始执行第3个任务");//2,3任务是并发执行的
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

    /**
     * countDown减为0才会执行第2类任务
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {
        CountDownLatch latchOne = new CountDownLatch(4);

        Thread[] threads = new Thread[8];
        for(int i=0;i<4;i++){
            threads[i] = new Thread(()->{
                LOGGER.info("开始执行第1类任务");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LOGGER.info("第1类执行完毕，释放锁");
                latchOne.countDown();
            });
        }

        for(int i=4;i<8;i++){
            threads[i] = new Thread(()->{
                LOGGER.info("准备执行第2类任务");
                try {
                    latchOne.await();//
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LOGGER.info("开始执行第2类任务");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LOGGER.info("第2类执行完毕");
            });
        }

        for (int i = 0; i < 8; i++) {
            threads[i].start();
        }
        System.in.read();
    }
}
