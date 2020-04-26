package com.steer.concurrent.queue.block;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * LinkedBlockingQueue 单向链表的无界队列(Integer.MAX_VALUE)
 * 底层入队和出队分别用了一把ReentrantLock
 *
 * 注意:入队速度远大于出队时,可能导致内存溢出
 */
public class LinkedBlockingQueueTest {
    Logger log = LoggerFactory.getLogger(LinkedBlockingQueueTest.class);

    /**
     * poll可能会获取到null
     */
    @Test
    public void test(){
        LinkedBlockingQueue<Integer> linkedBlockingQueue = new LinkedBlockingQueue<>();
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(new Producer(linkedBlockingQueue,"producer-1"));
        service.submit(new Producer(linkedBlockingQueue,"producer-2"));
        service.submit(new Producer(linkedBlockingQueue,"producer-3"));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        service.submit(new Consumer(linkedBlockingQueue,"consumer-1"));
        service.submit(new Consumer(linkedBlockingQueue,"consumer-2"));
        service.submit(new Consumer(linkedBlockingQueue,"consumer-3"));
        service.shutdown();
    }
}
