package com.steer.concurrent.queue.block;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * 传入对象必须满足Comparable接口
 * 取出的数据按照排序规则排好
 */
public class PriorityBlockingQueueTest {
    Logger log = LoggerFactory.getLogger(PriorityBlockingQueueTest.class);
    @Test
    public void test(){
        PriorityBlockingQueue<Integer> queue = new PriorityBlockingQueue();
        queue.add(23);
        queue.add(16);
        queue.add(6);
        queue.add(13);
        queue.add(3);
        queue.add(26);

        while (!queue.isEmpty()){
            try {
                log.info("take: {}",queue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
