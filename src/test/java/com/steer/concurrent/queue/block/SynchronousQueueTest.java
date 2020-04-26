package com.steer.concurrent.queue.block;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.SynchronousQueue;

/**
 * 一个内部只能包含一个元素的队列。
 *
 * 插入元素到队列的线程被阻塞，直到另一个线程从队列中获取了队列中存储的元素。
 * 同样，如果线程尝试获取元素并且当前不存在任何元素，则该线程将被阻塞，直到线程将元素插入队列。
 */
public class SynchronousQueueTest {
    Logger log = LoggerFactory.getLogger(SynchronousQueueTest.class);
    @Test
    public void teset(){
        SynchronousQueue queue = new SynchronousQueue();

        new Thread(()->{
            log.info("开始存放数据");
            try {
                queue.put(123);
                log.info("结束存放数据");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }).start();

        new Thread(()->{

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("开始取数据");
            try {
                Object take = queue.take();
                log.info("获取到数据:{}",take);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
