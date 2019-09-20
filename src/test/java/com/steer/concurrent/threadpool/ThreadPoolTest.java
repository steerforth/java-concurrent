package com.steer.concurrent.threadpool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.*;

public class ThreadPoolTest {
    private Logger LOGGER = LoggerFactory.getLogger(ThreadPoolTest.class);

    /**
     * 执行原理：
     * 新提交任务->判断corePoolSize是否满了（不满就新增线程）->满了判断queue是否满了(不满就放入等待队列中)
     * ->满了就判断maximumPoolSize是否满了（不满就新增线程）->满了就执行拒绝策略
     */

    /**
     * 当线程池和缓冲队列都被塞满了任务，但还有新的任务时，就会执行拒绝策略
     * 即执行的任务数量大于（最大线程池大小+缓冲队列大小）
     * 线程池的几种拒绝策略：
     * ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException
     * ThreadPoolExecutor.DiscardPolicy:丢弃任务，但是不抛出异常
     * ThreadPoolExecutor.DiscardOldestPolicy:丢弃队列最前面的任务，然后重新尝试执行任务(重复此过程)
     * ThreadPoolExecutor.CallerRunsPolicy:由调用线程处理该任务
     */
    @Test
    public void executorServicetest(){
        /**
         * corePoolSize:执行任务的线程
         * maximumPoolSize: 线程池最大允许的执行线程数量
         * keepAliveTime: 超过corePoolSize的线程，空闲线程存活时间；allowCoreThreadTimeOut(boolean)设为true，corePoolSize的线程也会受影响
         * 缓冲队列大小：大于corePoolSize的任务会放在缓冲队列中，
         * 所以值大于等于任务数-最大线程池数
         * ArrayBlockingQueue
         * LinkedBlokingQueue
         * SynchronousQueue
         * ArrayBlockingQueue
         * PriotityBlockingQueue
         * 当队列大小为Integer.MAX_VALUE时，maximumPoolSize和keepAliveTime参数意义就不大了，应该放在队列中了
         */
        ExecutorService pool = new ThreadPoolExecutor(2,6,0L, TimeUnit.SECONDS,new LinkedBlockingQueue<>(44),new ThreadFactoryBuilder().setNameFormat("mythread-%d").build());

        for (int i = 0; i <50; i++) {
            pool.execute(new MyRunnable(i));
        }
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
    }


    /**
     * 延迟执行，只执行1次任务
     */
    @Test
    public void scheduledExecutorServiceTest() throws IOException {
        ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(4,new BasicThreadFactory.Builder().namingPattern("mythread").daemon(false).build());
        scheduler.schedule(new MyRunnable(1),1, TimeUnit.SECONDS);
        //阻塞主线程
        System.in.read();
    }

    /**
     * 延迟2秒，周期1秒，
     * 执行任务的时间大于设置的周期时间，下一次的循环的执行任务会立马执行
     * 循环执行任务
     */
    @Test
    public void scheduledExecutorAtFixedRateTest() throws IOException {
        ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(4,new BasicThreadFactory.Builder().namingPattern("mythread").daemon(false).build());
        scheduler.scheduleAtFixedRate(new MyRunnable(1),1,3,TimeUnit.SECONDS);
        //阻塞主线程
        System.in.read();
    }

    /**
     * 等任务执行完后才延迟时间执行下一次
     * 循环执行任务
     * @throws InterruptedException
     */
    @Test
    public void scheduledExecutorWithFixedDelayTest() throws IOException {
        ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(4,new BasicThreadFactory.Builder().namingPattern("mythread").daemon(false).build());
        scheduler.scheduleWithFixedDelay(new MyRunnable(1),1,2,TimeUnit.SECONDS);
        //阻塞主线程
        System.in.read();
    }

    @Test
    public void CustomRejectedExecutionHandlerTest(){
        try {
            ExecutorService pool = new ThreadPoolExecutor(2,2,0L, TimeUnit.SECONDS,new LinkedBlockingQueue<>(1),new ThreadFactoryBuilder().setNameFormat("mythread-%d").build(),new MyRejectedExecutionHandler());
            for (int i = 0; i <5; i++) {
                pool.execute(new MyRunnable(i));
            }
        }catch (RejectedExecutionException e){
            LOGGER.error(e.getMessage());
            Assert.assertEquals(RejectedExecutionException.class,e.getClass());
        }
    }

    /**
     * 未处理
     * @throws InterruptedException
     */
//    @Test
//    public void AutoAdjustThreadPoolTest() throws InterruptedException {
//        AutoAdjustThreadPool pool = new AutoAdjustThreadPool();
//        pool.start(2,100,"自动",2);
//        for (int i = 0; i < 8; i++) {
//            pool.submit(new MyReapeatRunnable(i));
//            Thread.sleep(1000);
//        }
//        Thread.sleep(200000);
//    }

    /**
     * 任务为死循环，
     * 任务多余corePoolSize，多余的任务放在队列中等待执行
     * @throws InterruptedException
     */
    @Test
    public void manualAdjustThreadPoolTest() throws InterruptedException, IOException {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(2,2,60L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>(20),new ThreadFactoryBuilder().setNameFormat(new StringBuilder().append("manual-pool-").append("-%d").toString()).build());
        for (int i = 0; i < 8; i++) {
            pool.submit(new MyReapeatRunnable(i));
        }
        //动态增加线程池
        for (int i = 3; i <= 8; i++) {
            Thread.sleep(3000);
            pool.setCorePoolSize(i);
            pool.setMaximumPoolSize(i);
        }

        //阻塞主线程
        System.in.read();
    }
}
