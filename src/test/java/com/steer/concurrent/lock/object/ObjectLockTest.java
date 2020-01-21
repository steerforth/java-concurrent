package com.steer.concurrent.lock.object;

import com.steer.concurrent.lock.object.NotifyThread;
import com.steer.concurrent.lock.object.WaitThread;
import org.junit.Test;

import java.io.IOException;

public class ObjectLockTest {
    @Test
    public void test() throws IOException {
        Object lock = new Object();
        new Thread(new WaitThread(lock)).start();
        new Thread(new NotifyThread(lock)).start();
        System.in.read();
    }
}
