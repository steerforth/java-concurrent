package com.steer.concurrent.object;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 1.sleep()释放CPU的执行权，但不释放同步锁
 * 2.wait()释放CPU的执行权，同时也释放同步锁
 */
public class ObjectTest {
    private Logger log = LoggerFactory.getLogger(ObjectTest.class);

    private final Object lock = new Object();

    private boolean pause = false;

    @Test
    public void testWaitAndNotisfy() throws InterruptedException {
        MyRunnable r1 = new MyRunnable();
        MyRunnable r2 = new MyRunnable();

        Thread thread = new Thread(r1);
        thread.start();

        Thread thread2 = new Thread(r2);
        thread2.start();

        Thread.sleep(2000);

        //暂停
        pause = true;


        Thread.sleep(3000);

        //唤醒
        pause = false;
        synchronized (lock){
            lock.notifyAll();
        }

        Thread.sleep(5000);
    }

    class MyRunnable implements Runnable{

        @Override
        public void run() {
            while (true){
                log.info("=======do job=========");
                this.waitOrNot();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void waitOrNot() {
            if (pause){
                synchronized (lock){
                    try {
                        log.info("wait...");
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    log.info("被唤醒工作");
                }
            }
        }


//        public void pause(){
//            synchronized (this){
//                try {
//                    log.info("wait...");
//                    this.wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                log.info("被唤醒工作");
//            }
//        }
    }
}
