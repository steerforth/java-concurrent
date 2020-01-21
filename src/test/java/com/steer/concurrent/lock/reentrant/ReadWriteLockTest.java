package com.steer.concurrent.lock.reentrant;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/**
 * 读读共享;写写互斥;读写互斥;写读互斥;
 */
public class ReadWriteLockTest {
    private Logger log = LoggerFactory.getLogger(ReadWriteLockTest.class);
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * 读读共享
     */
    @Test
    public void testReadRead() throws IOException {
        new Thread(new ReadRunnable()).start();
        new Thread(new ReadRunnable()).start();

        System.in.read();
    }

    /**
     * 写写互斥
     */
    @Test
    public void testWriteWrite() throws IOException {
        new Thread(new WriteRunnable()).start();
        new Thread(new WriteRunnable()).start();

        System.in.read();
    }


    /**
     * 读写互斥
     */
    @Test
    public void testReadWrite() throws IOException {
        new Thread(new ReadRunnable()).start();

        //保证先读，再写
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(new WriteRunnable()).start();

        System.in.read();
    }

    /**
     * 读写互斥
     */
    @Test
    public void testWriteRead() throws IOException {
        new Thread(new WriteRunnable()).start();

        //保证先写，再读
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(new ReadRunnable()).start();

        System.in.read();
    }


    class ReadRunnable implements Runnable{

        @Override
        public void run() {
            lock.readLock().lock();

            log.info("=====开始做读取工作======");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("=====完成读取工作======");

            lock.readLock().unlock();
        }
    }

    class WriteRunnable implements Runnable{

        @Override
        public void run() {
            lock.writeLock().lock();

            log.info("=====开始做写入工作======");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("=====完成写入工作======");

            lock.writeLock().unlock();
        }
    }
}
