package com.steer.concurrent.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyReapeatRunnable implements Runnable{
    private Logger LOGGER = LoggerFactory.getLogger(MyReapeatRunnable.class);

    private int idx;

    public MyReapeatRunnable(int idx) {
        this.idx = idx;
    }
    @Override
    public void run() {
        while (true){
            LOGGER.info("我是{}",idx);
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
