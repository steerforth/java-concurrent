package com.steer.concurrent.queue.noblock;

import org.junit.Test;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 双向链表结构的无界并发队列
 *
 * 该阻塞队列同时支持FIFO和FILO两种操作方式
 */
public class ConcurrentLinkedDequeTest {

    @Test
    public void test(){
        Queue queue = new ConcurrentLinkedDeque();
        Object a = queue.poll();
        assert a == null ? true :false;
        queue.add("i am no.1");
        queue.add("i am no.2");
        queue.add("i am no.3");
        ((ConcurrentLinkedDeque) queue).addFirst("i am no.0");

        System.out.println("Currently the no.1 of the queue is："+((ConcurrentLinkedDeque) queue).getFirst());
        System.out.println("Currently the last of the queue is："+((ConcurrentLinkedDeque) queue).getLast());
        System.out.println("the size of the queue is:"+queue.size());
        //向队列尾部插入数据,队列个数会增加
        queue.offer("i am no.4");
        System.out.println("After offering,Currently the last of the queue is:"+((ConcurrentLinkedDeque) queue).getLast());
        System.out.println("the size of the queue is:"+queue.size());
        //从队列尾部取出数据,队列个数会减少
        String obj = (String) queue.poll();
        System.out.println("poll value:"+obj);
        System.out.println("After polling,Currently the last of the queue is:"+((ConcurrentLinkedDeque) queue).getLast());
        System.out.println("After polling,the size of the queue is:"+queue.size());

        //取出数据，但不会从队列中移除
        String obj2 = (String) queue.peek();
        System.out.println("peek value:"+obj2);
        System.out.println("After peeking,Currently the last of the queue is:"+((ConcurrentLinkedDeque) queue).getLast());
        System.out.println("After peeking,the size of the queue is:"+queue.size());

        //从队列中移除指定数据，如果存在返回true，反之false
        boolean success = queue.remove("i am no.100");
        System.out.println("removing success:"+success);
        System.out.println("After removing,Currently the last of the queue is:"+((ConcurrentLinkedDeque) queue).getLast());
        System.out.println("After removing,the size of the queue is:"+queue.size());
        boolean success2 = queue.remove("i am no.5");
        System.out.println("removing success:"+success2);
        System.out.println("After removing again,Currently the last of the queue is:"+((ConcurrentLinkedDeque) queue).getLast());
        System.out.println("After removing again,the size of the queue is:"+queue.size());


        for (Iterator iterator = queue.iterator(); iterator.hasNext();){
            System.out.println("In the queue,"+iterator.next().toString());
        }
    }


    @Test
    public void testConcurrent(){
        ConcurrentLinkedQueue<Integer> concurrentLinkedQueue = new ConcurrentLinkedQueue<Integer>();
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(new Producer(concurrentLinkedQueue,"producer-1"));
        service.submit(new Producer(concurrentLinkedQueue,"producer-2"));
        service.submit(new Producer(concurrentLinkedQueue,"producer-3"));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        service.submit(new Consumer(concurrentLinkedQueue,"consumer-1"));
        service.submit(new Consumer(concurrentLinkedQueue,"consumer-2"));
        service.submit(new Consumer(concurrentLinkedQueue,"consumer-3"));
        service.shutdown();
    }
}
