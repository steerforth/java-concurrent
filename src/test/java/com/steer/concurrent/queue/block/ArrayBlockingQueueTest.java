package com.steer.concurrent.queue.block;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 底层是数组的有界队列
 * 即添加操作和移除操作采用的同一个 ReentrantLock 锁
 *
 * 容量固定，有界队列，线程安全，阻塞特性
 */
public class ArrayBlockingQueueTest {
    static ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(10);
    @Test
    public void test() throws InterruptedException, IOException {
        new Thread(()->{
            try {
                Thread.sleep(1000);
                queue.put("1");
                queue.put("2");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }).start();

        new Thread(()->{
            try {
                String take = queue.take();
                System.out.println(">>>"+take);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        System.in.read();

    }
}
