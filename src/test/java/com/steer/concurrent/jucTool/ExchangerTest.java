package com.steer.concurrent.jucTool;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Exchanger;

/**
 * 交换器
 *
 * 应用:游戏交换装备
 */
public class ExchangerTest {
    Logger log = LoggerFactory.getLogger(ExchangerTest.class);

    Exchanger<String> exchanger = new Exchanger<>();
    @Test
    public void test(){
        new Thread(()->{
            String s = "T1";
            try {
                //先执行的线程，阻塞；等待另一个线程调用exchange方法
                s = exchanger.exchange(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("[{}]  值:{}",Thread.currentThread().getName(),s);
        },"t1").start();

        new Thread(()->{
            String s = "T2";
            try {
                //后执行的线程，把s塞进去，完成两个线程间的数据交换
                s = exchanger.exchange(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("[{}]  值:{}",Thread.currentThread().getName(),s);
        },"t2").start();

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
