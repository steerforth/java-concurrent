package com.steer.concurrent.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class MyRejectedExecutionHandler implements RejectedExecutionHandler {
    private Logger LOGGER = LoggerFactory.getLogger(MyRejectedExecutionHandler.class);

    /**
     *
     * @param r 新传入的被拒绝的线程
     * @param e
     */
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
//        LOGGER.info("{}",r);
//        LOGGER.info("{}",e.getTaskCount());
//        ThreadPoolExecutor.DiscardOldestPolicy
        throw new RejectedExecutionException("Task " + r.toString() +
                " rejected from " +
                e.toString());
    }
}
