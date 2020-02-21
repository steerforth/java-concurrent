package com.steer.concurrent.lock.object;

import com.steer.concurrent.lock.object.NotifyThread;
import com.steer.concurrent.lock.object.WaitThread;
import org.junit.Test;

import java.io.IOException;

public class ObjectLockTest {
    @Test
    public void test() throws IOException {
        //同一把锁
        Object lock = new Object();
        Thread waitThread = new Thread(new WaitThread(lock));
        waitThread.start();


//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        waitThread.interrupt();

        Thread notifyThread = new Thread(new NotifyThread(lock));
        notifyThread.start();
        System.in.read();
    }
}
