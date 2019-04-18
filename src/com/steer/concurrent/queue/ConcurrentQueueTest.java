package com.steer.concurrent.queue;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ConcurrentQueueTest {

    public static void QueueAddAndRemoveTest(){

        Queue queue = new ConcurrentLinkedDeque();
        queue.add("i am no.1");
        queue.add("i am no.2");
        queue.add("i am no.3");
        ((ConcurrentLinkedDeque) queue).addFirst("i am no.0");

        System.out.println("Currently the no.1 of the queue is："+((ConcurrentLinkedDeque) queue).getFirst());
        System.out.println("Currently the last of the queue is："+((ConcurrentLinkedDeque) queue).getLast());
        System.out.println("the size of the queue is:"+queue.size());
        //向队列尾部插入数据
        queue.offer("i am no.4");
        System.out.println("After offering,Currently the last of the queue is:"+((ConcurrentLinkedDeque) queue).getLast());
        System.out.println("the size of the queue is:"+queue.size());
        //从队列尾部取出数据
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

    }

    public static void main(String[] args) {
        QueueAddAndRemoveTest();
    }
}
