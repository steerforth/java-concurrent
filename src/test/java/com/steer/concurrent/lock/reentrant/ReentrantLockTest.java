package com.steer.concurrent.lock.reentrant;

import com.steer.concurrent.lock.reentrantLock.T_ReentrantLock;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock相比synchronized使用上更加灵活
 *
 * 1.公平锁是指当锁可用时,在锁上等待时间最长的线程将获得锁的使用权。而非公平锁则随机分配这种使用权,非公平锁性能更好
 * 2.可重入锁，同一个线程可多次获取同一把锁，但相应得也要释放获取的次数
 */
public class ReentrantLockTest {

    private Logger LOGGER = LoggerFactory.getLogger(ReentrantLockTest.class);

    final ReentrantLock lock = new ReentrantLock();
    final Condition condition = lock.newCondition();

    @Test
    public void testLockCondition() throws IOException {

        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        consumer.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        producer.start();
        System.in.read();

    }

    @Test
    public void testFairLock() throws IOException {
        new Thread(new T_ReentrantLock()).start();
        new Thread(new T_ReentrantLock()).start();
        System.in.read();
    }

    @Test
    public void testLockInterruptibly() throws IOException, InterruptedException {
        Lock lock = new ReentrantLock();
        new Thread(()->{
            try {
                lock.lock();
                LOGGER.info("t1 start");
                TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
                LOGGER.info("t1 end");
            }catch (InterruptedException e){
                LOGGER.info("t1 interrupted!");
            }finally {
                lock.unlock();
            }
        }).start();

        Thread t2 = new Thread(()->{
            try {
                LOGGER.info("t2 before start");
                lock.lockInterruptibly();//尝试获得锁；如果线程中断，则抛出异常
//                lock.lock();
                LOGGER.info("t2 start");
                TimeUnit.SECONDS.sleep(1);
                LOGGER.info("t2 end");
            }catch (InterruptedException e){
                LOGGER.info("t2 interrupted!");
            }finally {
                lock.unlock();
            }
        });
        t2.start();

        LOGGER.info("sleep");
        TimeUnit.SECONDS.sleep(5);

        t2.interrupt();

        System.in.read();
    }

    class Consumer extends Thread{

        @Override
        public void run() {
            consume();
        }

        private void consume() {

            try {
                lock.lock();
                LOGGER.info("consumer获取到锁");
                Thread.sleep(500);
                LOGGER.info("consumer进入等待");
                /**
                 * await方法会让producer的获取到lock
                 */
                condition.await();
                LOGGER.info("consumer被唤醒了");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally{
                lock.unlock();
                LOGGER.info("consumer释放锁");
            }

        }
    }

    class Producer extends Thread{

        @Override
        public void run() {
            produce();
        }

        private void produce() {
            try {
                LOGGER.info("Producer等待获取锁");
                lock.lock();
                LOGGER.info("Producer获取到锁");

                LOGGER.info("Producer通知consumer");
                condition.signal();
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally{
                lock.unlock();
                LOGGER.info("Producer释放锁");
            }
        }
    }


}
