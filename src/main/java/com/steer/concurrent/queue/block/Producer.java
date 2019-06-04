package com.steer.concurrent.queue.block;

import java.util.concurrent.LinkedBlockingQueue;

public class Producer implements Runnable{
    private LinkedBlockingQueue queue;
    private String name;
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("producer:"+this.name+" add value:"+i);
            try {
                queue.put(i);
                Thread.sleep(200);//模拟慢生产速度
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public Producer(LinkedBlockingQueue queue, String name) {
        this.queue = queue;
        this.name = name;
    }

    public LinkedBlockingQueue getQueue() {
        return queue;
    }

    public void setQueue(LinkedBlockingQueue queue) {
        this.queue = queue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
