package com.steer.concurrent.threadpool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 自动调整线程池的核心线程数
 */
public class AutoAdjustThreadPool {

    /**
     * 队列阈值，超过此值则扩大线程池
     */
    private static final int MAX_QUEUE_SIZE = 3;

    /**
     * 每次扩容自动增加线程数
     */
    private static final int PER_ADD_THREAD = 10;

    /**
     * 监控积压时间频率
     */
    private static final int MONITOR_DELAY_TIME = 1;

    private ThreadPoolExecutor pool;

    private ScheduledExecutorService adjustService;

    public void start(int poolSize,int maxPoolSize, String threadName, int queueSize){
        pool = new ThreadPoolExecutor(poolSize,maxPoolSize,60L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>(queueSize),new ThreadFactoryBuilder().setNameFormat(new StringBuilder().append(threadName).append("-%d").toString()).build());
        adjustService = new ScheduledThreadPoolExecutor(1, new BasicThreadFactory.Builder().namingPattern("adjust-pool-%d").daemon(true).build());
        adjustService.scheduleWithFixedDelay(new AdjustRunnble(pool),MONITOR_DELAY_TIME,MONITOR_DELAY_TIME, TimeUnit.SECONDS);

    }

    public <T> Future<T> submit(Callable<T> task) {
        return pool.submit(task);
    }
    public Future<?> submit(Runnable task) {
        return pool.submit(task);
    }

    public void stop() throws InterruptedException {
        pool.shutdown();
        while (!pool.awaitTermination(1,TimeUnit.SECONDS)){
            //等待线程池中任务执行完毕
        }
        adjustService.shutdown();
    }

    private class AdjustRunnble implements  Runnable{
        private Logger LOGGER = LoggerFactory.getLogger(AdjustRunnble.class);
        private ThreadPoolExecutor pool;

        public AdjustRunnble(ThreadPoolExecutor pool) {
            this.pool= pool;
        }

        @Override
        public void run() {
            LOGGER.info("当前线程池状态:"+pool);
            //当队列大小超过限制，且jvm内存使用率小于80%时扩容，防止无限制扩容
//            if(pool.getQueue().size() >= MAX_QUEUE_SIZE && pool.getPoolSize()< pool.getMaximumPoolSize() && getMemoryUsage()<0.8){
//                LOGGER.info("线程池扩容！"+pool);
//                pool.setCorePoolSize(pool.getPoolSize() + PER_ADD_THREAD);
//            }
//            //当队列大小小于限制的80%，线程池缩容
//            if(pool.getPoolSize() > 0  && pool.getQueue().size() < MAX_QUEUE_SIZE * 0.8 ){
//                LOGGER.info("线程池缩容！"+pool);
//                pool.setCorePoolSize(pool.getPoolSize() - PER_ADD_THREAD);
//            }
        }

        public double getMemoryUsage() {
            return (double) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / Runtime.getRuntime().maxMemory();
        }
    }



}
