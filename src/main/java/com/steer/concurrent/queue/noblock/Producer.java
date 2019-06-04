package com.steer.concurrent.queue.noblock;

import java.util.Queue;

public class Producer implements Runnable{
    private Queue queue;
    private String name;

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("producer:"+this.name+" add value:"+i);
            queue.add(i);
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public Producer(Queue queue, String name) {
        this.queue = queue;
        this.name = name;
    }

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
