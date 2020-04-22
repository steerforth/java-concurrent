package com.steer.concurrent.atomic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.LongAdder;

public class LongAdderTest {
    private Logger log = LoggerFactory.getLogger(LongAdderTest.class);
    static LongAdder count = new LongAdder();

    /**
     * 分段锁
     * 线程数多,循环数多  比较有优势
     * @throws InterruptedException
     */
    @org.junit.Test
    public void test() throws InterruptedException {
        Thread[] threads = new Thread[1000];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(()->{
                for (int j = 0; j < 100000; j++) {
                    count.increment();
                }
            });
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }

        log.info("最后值:{}",count.longValue());

    }
}
