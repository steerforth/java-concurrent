package com.steer.concurrent.object;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * join的本质是wait(),锁的对象是ThreadA
 * 等线程A执行完毕后，notifyAll()
 */
public class JoinTest {
    Logger log = LoggerFactory.getLogger(JoinTest.class);

    /**
     * 多线程按顺序执行
     */
    @Test
    public void test(){
        log.info("主线程开启.");

        //启动一个子线程
        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                log.info("线程A开启.");
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                log.info("线程A执行完毕.");
            }
        });
        threadA.start();



        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                log.info("线程B准备.");
                try {
                    threadA.join();
                    log.info("线程B开启.");
                    Thread.sleep(200);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                log.info("线程B执行完毕.");
            }
        });
        threadB.start();
        log.info("主线程调用join之前");
        try {
            threadB.join();    //调用join()
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("主线程完成.");
    }
}
