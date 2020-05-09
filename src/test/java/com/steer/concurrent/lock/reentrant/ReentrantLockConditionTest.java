package com.steer.concurrent.lock.reentrant;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockConditionTest {
    private Logger log = LoggerFactory.getLogger(ReentrantLockConditionTest.class);
    @Test
    public void test() throws IOException {
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

    public class BookStore {
        private ArrayList books = new ArrayList();
        private ReentrantLock lock = new ReentrantLock(false);
        private Condition buyCondition = lock.newCondition();
        private Condition sellCondition = lock.newCondition();
        public void addBook() {
            lock.lock();
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
        }

        public void removeBook() {
            lock.lock();
            while (books.size() <= 0) {
                try {
                    log.info("买家[{}]等待购入图书",Thread.currentThread().getName());
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
