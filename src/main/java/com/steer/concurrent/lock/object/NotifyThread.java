package com.steer.concurrent.lock.object;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotifyThread implements Runnable {

    private Logger LOGGER = LoggerFactory.getLogger(NotifyThread.class);
    private final Object lock;

    public NotifyThread(Object lock) {
        this.lock = lock;
    }

    @Override
    public void run() {

        synchronized (lock){
            LOGGER.info("notify做一些事情");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOGGER.info("notify开始通知");
            lock.notify();
            LOGGER.info("notify结束通知");
        }
    }
}
