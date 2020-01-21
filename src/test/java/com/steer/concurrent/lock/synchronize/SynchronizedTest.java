package com.steer.concurrent.lock.synchronize;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SynchronizedTest {

    private Logger LOGGER = LoggerFactory.getLogger(SynchronizedTest.class);

    /**
     * add1,add3方法都不安全；
     * add1锁的对象是this，而创建的是threa1,thread2两个对象，实际两把锁
     * add3锁的对象是object,每个threa1,thread2都各自持久一个。如果object用static修饰，会变成安全
     * add2锁的对象是SynchronizedThread.class，故安全
     */
    @Test
    public void test(){
        SynchronizedThread thread1 = new SynchronizedThread();
        SynchronizedThread thread2 = new SynchronizedThread();

        ExecutorService pool = new ThreadPoolExecutor(2,6,0L, TimeUnit.SECONDS,new LinkedBlockingQueue<>(44),new ThreadFactoryBuilder().setNameFormat("mythread-%d").build());

        pool.submit(thread1);
        pool.submit(thread2);

        //不在接收新的任务
        pool.shutdown();

        try {
            //每隔1秒轮询  线程池中的任务是否都完成了
            while(!pool.awaitTermination(1,TimeUnit.SECONDS)){
                LOGGER.info("仍有线程任务未完成");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("all over");
        LOGGER.info("end: a= "+thread1.getValue());
    }

}
