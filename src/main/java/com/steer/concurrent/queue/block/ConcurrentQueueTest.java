package com.steer.concurrent.queue.block;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class ConcurrentQueueTest {


    /**
     * poll可能会获取到null
     */
    public static void currentTest(){
//        ScheduledThreadPoolExecutor dataLoadExcutor = new ScheduledThreadPoolExecutor(2,
//                new BasicThreadFactory.Builder().namingPattern("gabriel-msg-schedule-pool-%d").daemon(false).build());


//        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
//                .setNameFormat("demo-pool-%d").build();

//        //Common Thread Pool
//        ExecutorService pool = new ThreadPoolExecutor(5, 200,
//                0L, TimeUnit.MILLISECONDS,
//                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
//
//        pool.execute(()-> System.out.println(Thread.currentThread().getName()));
//        pool.shutdown();//gracefully shutdown

        LinkedBlockingQueue<Integer> linkedBlockingQueue = new LinkedBlockingQueue<>();
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(new Producer(linkedBlockingQueue,"producer-1"));
        service.submit(new Producer(linkedBlockingQueue,"producer-2"));
        service.submit(new Producer(linkedBlockingQueue,"producer-3"));

//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        service.submit(new Consumer(linkedBlockingQueue,"consumer-1"));
        service.submit(new Consumer(linkedBlockingQueue,"consumer-2"));
        service.submit(new Consumer(linkedBlockingQueue,"consumer-3"));
        service.shutdown();
    }


    public static void main(String[] args) {
        currentTest();
    }
}
