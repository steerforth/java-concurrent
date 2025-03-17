package com.steer.concurrent.lock.reentrant;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockConditionTest {
    private Logger log = LoggerFactory.getLogger(ReentrantLockConditionTest.class);
    @Test
    public void testBookStore() throws IOException {
        BookStore bookStore = new BookStore();
        Buyer[] buyers = new Buyer[5];
        Seller sellers;
        //创建5个买家线程，负责买书
        for(int i = 0; i < 5; i++) {
            buyers[i] = new Buyer(bookStore);
        }
        //创建售货员线程，负责进书
        sellers = new Seller(bookStore);
        //启动线程
        sellers.start();
        for(int i = 0; i < 5; i++) {
            buyers[i].start();
        }
        System.in.read();
    }

    @Test
    public void test2() throws IOException {
        Lock lock = new ReentrantLock();
        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();

        new Thread(() -> {
            lock.lock();
            try {
                log.info("Thread2 await");
                condition1.await(); // 等待 Thread 1 完成
                log.info("Thread2 do");
                condition2.signal(); // 唤醒等待 condition2 的线程
                log.info("Thread2 signal");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finally { lock.unlock(); }
        }).start();

        new Thread(() -> {
            lock.lock();
            try {
                log.info("Thread3 await");
                condition2.await(); // 等待 Thread 2 完成
                log.info("Thread3 do");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finally { lock.unlock(); }
        }).start();
        //这段代码放最前面就跑不动了？
        new Thread(() -> {
            lock.lock();
            try {
                log.info("Thread1 do");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {e.printStackTrace();}
                condition1.signal(); // 唤醒等待 condition1 的线程
                log.info("Thread1 signal");
            }
            finally { lock.unlock(); }
        }).start();

        System.in.read();
    }

    public class BookStore {
        private ArrayList books = new ArrayList();
        private ReentrantLock lock = new ReentrantLock(false);
        private Condition buyCondition = lock.newCondition();
        private Condition sellCondition = lock.newCondition();
        public void addBook() {
            lock.lock();
            log.info("售货员[{}]获取到锁",Thread.currentThread().getName());
            while (books.size() >= 1) {
                try {
                    log.info("售货员等待图书售出");
                    sellCondition.await();  //售货员等待书店出现空位
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            books.add(1);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("进货了一本书，剩余:{}",books.size());
            buyCondition.signal();  //通知买家买书
            lock.unlock();
            log.info("售货员释放锁");
        }

        public void removeBook() {
            lock.lock();
            log.info("买家[{}]获取到锁",Thread.currentThread().getName());
            while (books.size() <= 0) {
                try {
                    log.info("买家[{}]等待购入图书,释放锁进入条件等待队列",Thread.currentThread().getName());
                    buyCondition.await();  //买家等待书店进书
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            books.remove(0);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("买家[{}]购入了一本书，剩余:{}",Thread.currentThread().getName(),books.size());
            sellCondition.signal();  //通知售货员进书
            lock.unlock();
            log.info("买家释放锁");
        }
    }

    public class Buyer extends Thread {
        private BookStore bookStore;

        public Buyer(BookStore bookStore) {
            this.bookStore = bookStore;
        }

        @Override
        public void run() {
            while (true) {
                bookStore.removeBook();
            }
        }
    }

    public class Seller extends Thread {
        private BookStore bookStore;
        public Seller(BookStore bookStore) {
            this.bookStore = bookStore;
        }

        @Override
        public void run() {
            while (true) {
                bookStore.addBook();
            }
        }
    }
}
