package com.steer.concurrent.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyRunnable implements Runnable {
    private Logger LOGGER = LoggerFactory.getLogger(MyRunnable.class);

    private int idx;

    public MyRunnable(int idx) {
        this.idx = idx;
    }
    @Override
    public void run() {
        LOGGER.info("我是{}",idx);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
