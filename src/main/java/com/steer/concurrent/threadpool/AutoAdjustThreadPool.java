package com.steer.concurrent.threadpool;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class AutoAdjustThreadPool {

    private ExecutorService pool;

    private ScheduledExecutorService adjustService;

    public void start(int poolSize, String threadName, int queueSize){
        pool = SteerExecutors.newFixedThreadPool(poolSize,threadName,queueSize);
        adjustService = new ScheduledThreadPoolExecutor(10, new BasicThreadFactory.Builder().namingPattern("mq-monitor-schedule-pool-%d").daemon(true).build());
    }

}
