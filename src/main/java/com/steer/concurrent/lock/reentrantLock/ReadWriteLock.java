package com.steer.concurrent.lock.reentrantLock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读读共享;写写互斥;读写互斥;写读互斥;
 */
public class ReadWriteLock {
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    class ReadThread implements Runnable{

        @Override
        public void run() {
            lock.readLock().lock();

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            lock.readLock().unlock();
        }
    }
}
