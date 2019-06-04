package com.steer.concurrent.queue.noblock;

import java.util.Queue;

public class Consumer implements Runnable{
    private Queue queue;
    private String name;
    

    public Consumer(Queue queue, String name) {
        this.queue = queue;
        this.name = name;
    }

    @Override
    public void run() {

        for (int i = 0; i < 10; i++) {
            System.out.println( "consumer:"+this.name+" poll value:"+this.queue.poll());
        }
        
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
