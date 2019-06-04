package com.steer.concurrent.queue.block;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Consumer implements Runnable{
    private LinkedBlockingQueue queue;
    private String name;


    public Consumer(LinkedBlockingQueue queue, String name) {
        this.queue = queue;
        this.name = name;
    }
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            long t1 = System.currentTimeMillis();
            //poll 取值 不会阻塞线程  队列中没有数据会返回null值
//            System.out.println( "consumer:"+this.name+" poll value:"+this.queue.poll());
            try {
                //take 取数据会 阻塞线程
                System.out.println( "consumer:"+this.name+" take value:"+this.queue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long t2 = System.currentTimeMillis();
            System.out.println("consumer:"+this.name+" cost time:"+(t2-t1));
        }
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
