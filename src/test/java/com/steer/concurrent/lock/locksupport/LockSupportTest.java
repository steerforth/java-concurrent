package com.steer.concurrent.lock.locksupport;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class LockSupportTest {
    Logger log = LoggerFactory.getLogger(LockSupport.class);
    @Test
    public void test() throws IOException {
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                log.info("值:{}",i);
                if (i == 5){
                    LockSupport.park();
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        System.in.read();
    }

    @Test
    public void test2() throws IOException {
        Thread t = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                log.info("值:{}", i);
                if (i == 5) {
                    LockSupport.park();
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        //unpark先或后于 park方法执行都生效
        LockSupport.unpark(t);

        System.in.read();
    }

    /**
     * 第2次park后,会被一直阻塞
     * @throws IOException
     */
    @Test
    public void test3() throws IOException {
        Thread t = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                log.info("值:{}", i);
                if (i == 5) {
                    LockSupport.park();
                }

                if (i == 8) {
                    LockSupport.park();
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        //unpark先或后于 park方法执行都生效
        //如果连续2次调用unpark方法,其实都是将__count置为1,不会累加的
        LockSupport.unpark(t);
        LockSupport.unpark(t);

        System.in.read();
    }

    /**
     * 实际LockSupport维护了一个状态变量,来看是否要阻塞当前线程
     * @throws IOException
     */
    @Test
    public void test4() throws IOException {
        Thread t = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                log.info("值:{}", i);
                if (i == 5) {
                    LockSupport.park();
                }

                if (i == 8) {
                    LockSupport.park();
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();

        LockSupport.unpark(t);

        try {
            TimeUnit.SECONDS.sleep(12);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //等i=8,park后,把__count置为0,再调用unpark置为1就有效果了
        LockSupport.unpark(t);

        System.in.read();
    }
}
