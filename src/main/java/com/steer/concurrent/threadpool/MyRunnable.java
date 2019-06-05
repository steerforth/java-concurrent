package com.steer.concurrent.threadpool;

public class MyRunnable implements Runnable {
    private int idx;

    public MyRunnable(int idx) {
        this.idx = idx;
    }
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+"我是"+idx);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
