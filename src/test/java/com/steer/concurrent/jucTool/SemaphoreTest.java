package com.steer.concurrent.jucTool;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

/**
 * 信号灯
 *
 * 限流
 */
public class SemaphoreTest {
    private Logger log = LoggerFactory.getLogger(SemaphoreTest.class);

    @Test
    public void test(){

        //同时允许2个线程执行
        Semaphore s = new Semaphore(2,true);
        new Thread(()->{
            try {
                //阻塞方法
                s.acquire();
                log.info("T1 running start");
                Thread.sleep(200);
                log.info("T1 running end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                s.release();
            }
        }).start();
        new Thread(()->{
            try {
                s.acquire();
                log.info("T2 running start");
                Thread.sleep(200);
                log.info("T2 running end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                s.release();
            }
        }).start();
        new Thread(()->{
            try {
                s.acquire();
                log.info("T3 running start");
                Thread.sleep(200);
                log.info("T3 running end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                s.release();
            }
        }).start();

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
