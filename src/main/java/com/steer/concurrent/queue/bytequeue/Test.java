package com.steer.concurrent.queue.bytequeue;

import java.util.Date;

public class Test {
    public static void main(String[] args) {
        new Thread(new MyThread()).start();
    }

    static class MyThread implements Runnable{
        @Override
        public void run() {
            System.out.println(new Date()+" start!");
            try {
                this.wait(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(new Date()+" end!");
            System.out.println(111);
        }
    }
}
