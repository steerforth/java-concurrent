package com.steer.concurrent.lock.object;

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
            LOGGER.info("wait在等候");
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOGGER.info("wait收到lock notify通知，可以开始做事情");
        }
    }
}
