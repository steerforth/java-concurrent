package com.steer.concurrent.lock.stampedlock;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.locks.StampedLock;

/**
 * 对ReentrantReadWriteLock的改进
 *
 * 不可重入
 *
 * 乐观读模式
 */
public class StampedLockTest {
    private Logger log = LoggerFactory.getLogger(StampedLockTest.class);
    // 成员变量
    private double x, y;

    // 锁实例
    private final StampedLock sl = new StampedLock();

    @Test
    public void testRW(){
        new Thread(new ReadRunnable()).start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(new WriteRunnable()).start();

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRR(){
        new Thread(new ReadRunnable()).start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(new ReadRunnable()).start();

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWR(){
        new Thread(new WriteRunnable()).start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(new ReadRunnable()).start();

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWW(){
        new Thread(new WriteRunnable()).start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(new WriteRunnable()).start();

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 排它锁-写锁（writeLock）
    void move(double deltaX, double deltaY) {
        long stamp = sl.writeLock();
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            sl.unlockWrite(stamp);
        }
    }

    // 一个只读方法
    // 其中存在乐观读锁到悲观读锁的转换
    double distanceFromOrigin() {

        // 尝试获取乐观读锁
        long stamp = sl.tryOptimisticRead();
        // 将全部变量拷贝到方法体栈内
        double currentX = x, currentY = y;
        // 检查在获取到读锁stamp后，锁有没被其他写线程抢占
        if (!sl.validate(stamp)) {
            // 如果被抢占则获取一个共享读锁（悲观获取）
            stamp = sl.readLock();
            try {
                // 将全部变量拷贝到方法体栈内
                currentX = x;
                currentY = y;
            } finally {
                // 释放共享读锁
                sl.unlockRead(stamp);
            }
        }
        // 返回计算结果
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }


    // 获取读锁，并尝试转换为写锁
    void moveIfAtOrigin(double newX, double newY) {
        long stamp = sl.tryOptimisticRead();
        try {
            // 如果当前点在原点则移动
            while (x == 0.0 && y == 0.0) {
                // 尝试将获取的读锁升级为写锁
                long ws = sl.tryConvertToWriteLock(stamp);
                // 升级成功，则更新stamp，并设置坐标值，然后退出循环
                if (ws != 0L) {
                    stamp = ws;
                    x = newX;
                    y = newY;
                    break;
                } else {
                    // 读锁升级写锁失败则释放读锁，显示获取独占写锁，然后循环重试
                    sl.unlockRead(stamp);
                    stamp = sl.writeLock();
                }
            }
        } finally {
            sl.unlock(stamp);
        }
    }


    class ReadRunnable implements Runnable{

        @Override
        public void run() {
            long stamp = sl.tryOptimisticRead();


            log.info("=====开始做读取工作======");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("=====完成读取工作======");

            if (!sl.validate(stamp)){
                stamp = sl.readLock();
                try {
                    log.info("=====开始做重新悲观读取工作======");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    log.info("=====完成悲观读取工作======");
                }finally {
                    sl.unlockRead(stamp);
                }
            }
        }
    }

    class WriteRunnable implements Runnable{

        @Override
        public void run() {
            long stamp = sl.writeLock();

            log.info("=====开始做写入工作======");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("=====完成写入工作======");

            sl.unlockWrite(stamp);
        }
    }
}
