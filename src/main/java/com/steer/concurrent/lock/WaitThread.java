package com.steer.concurrent.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaitThread implements Runnable{
    private Logger LOGGER = LoggerFactory.getLogger(WaitThread.class);
    private Object lock;

    public WaitThread(Object lock) {
        this.lock = lock;
    }
    @Override
    public void run() {
        synchronized (lock){
            LOGGER.info("我在等候");
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOGGER.info("收到开锁通知，开始做事情");
        }
    }
}
