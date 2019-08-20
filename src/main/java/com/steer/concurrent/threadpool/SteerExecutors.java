package com.steer.concurrent.threadpool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.*;

public class SteerExecutors {
    /**
     * 注意提交的执行任务数应该小于poolSize+queueSize，不然会被执行拒绝策略
     * @param poolSize
     * @param threadName
     * @param queueSize
     * @return
     */
    public static ThreadPoolExecutor newFixedThreadPool(int poolSize, String threadName, int queueSize){
        return new ThreadPoolExecutor(poolSize,poolSize,0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>(queueSize),new ThreadFactoryBuilder().setNameFormat(new StringBuilder().append(threadName).append("-%d").toString()).build());
    }

    public static ScheduledThreadPoolExecutor newScheduledThreadPool(int poolSize, String threadName){
        return new ScheduledThreadPoolExecutor(poolSize,new BasicThreadFactory.Builder().namingPattern(new StringBuilder().append(threadName).append("-%d").toString()).daemon(false).build());
    }

    public static ScheduledThreadPoolExecutor newSingleThreadScheduledExecutor(String threadName){
        return new ScheduledThreadPoolExecutor(1,new BasicThreadFactory.Builder().namingPattern(new StringBuilder().append(threadName).append("-%d").toString()).daemon(false).build());
    }
}
