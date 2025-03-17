package com.steer.concurrent.jucTool;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 循环栅栏
 *
 * 等线程任务满了后(达到限定值)，一起执行
 *
 * 执行过程不会阻塞主线程
 */
public class CyclicBarrierTest {
    Logger log = LoggerFactory.getLogger(CyclicBarrierTest.class);
    @Test
    public void test(){
        CyclicBarrier barrier = new CyclicBarrier(4,()->{
            log.info("=============满人开车============");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("=============满人开车2============");
        });
        //获取等待的线程
//        barrier.getNumberWaiting();
        for (int i = 0; i < 20; i++) {
              new Thread(()->{
                  try {
                      log.info("等候开车");
                      barrier.await();
                      log.info("----------->出发");
                      Thread.sleep(2000);
                      log.info("----------->出发结束");
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  } catch (BrokenBarrierException e) {
                      e.printStackTrace();
                  }
              }).start();
        }
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
