package com.steer.concurrent.lock;

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
