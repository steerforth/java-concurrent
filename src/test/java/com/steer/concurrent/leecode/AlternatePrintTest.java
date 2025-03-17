package com.steer.concurrent.leecode;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 1115题 交替打印foo bar
 * 1.ReetrantLock Condition
 * 2.LinkedBlockingQueue
 */
public class AlternatePrintTest {
    Logger LOGGER = LoggerFactory.getLogger(AlternatePrintTest.class);

    class FooBar {
        private int n;
        ReentrantLock lock = new ReentrantLock(true);
        Condition fooCondition = lock.newCondition();
        Condition barCondition = lock.newCondition();

        private volatile boolean flag = false;

        public FooBar(int n) {
            this.n = n;
        }

        public void foo(Runnable printFoo) throws InterruptedException {
            for (int i = 0; i < n; i++) {

//                LOGGER.info("foo1");
                lock.lock();
//                LOGGER.info("foo2");
                while (flag) {
                    // printBar.run() outputs "bar". Do not change or remove this line.
                    fooCondition.await();
                }

                printFoo.run();
                flag = true;

                try {
                    Thread.sleep(100);
                    barCondition.signal();
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                LOGGER.info("foo3");
                lock.unlock();
//                LOGGER.info("foo4");
            }

        }

        public void bar(Runnable barFoo) throws InterruptedException {

            for (int i = 0; i < n; i++) {

//                LOGGER.info("bar1");
                lock.lock();
//                LOGGER.info("bar2");
                while (!flag) {
                    // printBar.run() outputs "bar". Do not change or remove this line.
                    barCondition.await();
                }

                barFoo.run();
                flag = false;

                try {
                    Thread.sleep(100);
                    fooCondition.signal();
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                LOGGER.info("bar3");
                lock.unlock();
//                LOGGER.info("bar4");
            }

        }

    }

    class printFoo implements Runnable{
        @Override
        public void run() {
            LOGGER.info("foo");
        }
    }

    class printBar implements Runnable{
        @Override
        public void run() {
            LOGGER.info("bar");
        }
    }

    @Test
    public void testReentrantLock() throws IOException, InterruptedException {
        FooBar fooBar = new FooBar(10);
        new Thread(() -> {

            try {
                fooBar.foo(new printFoo());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                fooBar.bar(new printBar());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();


        System.in.read();
    }



}
