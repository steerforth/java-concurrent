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
        //未获取到lock对象的锁的线程会进入该lock对象的锁池中
        synchronized (lock){
            LOGGER.info("wait在等候");
            try {
                //调用wait的线程会进入该对象的等待池中
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOGGER.info("wait收到lock notify通知，可以开始做事情");
        }
    }
}
