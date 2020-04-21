package com.steer.concurrent.lock.reentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.ReentrantLock;

public class T_ReentrantLock implements Runnable{
    Logger log = LoggerFactory.getLogger(T_ReentrantLock.class);
    //公平锁
    private static ReentrantLock lock = new ReentrantLock(true);

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            lock.lock();
            try {
                log.info("获得锁");
            }finally {
                lock.unlock();
            }

        }
    }
}
