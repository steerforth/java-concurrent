package com.steer.concurrent.atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AtomicTest implements Runnable{
    private static int i;
    /**
     * volatile修饰的变量，保证了多个线程在调用这个变量时，都是从主内存中读取的，即是最新的值，但不能保证多线程执行的有序性，即不能保证并发的正确性，不同于synchronized
     */
    private static volatile Integer vi = 0;
    /**
     * 原子类,底层是CAS,change and swap
     */
    private static AtomicInteger ai = new AtomicInteger();

    private static Integer si = 0;

    private static int ri;

    private static AtomicInteger flag = new AtomicInteger();

    //没用static修饰，实际用的是两把锁
    private Lock lock = new ReentrantLock();

    private static Object object = new Object();

    @Override
    public void run() {
        for(int k=0;k<200000;k++){
            i++;
            vi++;
            ai.incrementAndGet();
            synchronized(si){
                si++;
            }
            lock.lock();
            try{
                ri++;
            }finally{
                lock.unlock();
            }
        }
        flag.incrementAndGet();
    }


    public static void main(String[] args) throws InterruptedException{
        AtomicTest t1 = new AtomicTest();
        AtomicTest t2 = new AtomicTest();
        ExecutorService exec1 = Executors.newCachedThreadPool();
        ExecutorService exec2 = Executors.newCachedThreadPool();
        exec1.execute(t1);
        exec2.execute(t2);
        while(true){
            if(flag.intValue()==2){
                System.out.println("i>>>>>"+i);
                System.out.println("vi>>>>>"+vi);
                System.out.println("ai>>>>>"+ai);
                System.out.println("si>>>>>"+si);
                System.out.println("ri>>>>>"+ri);
                break;
            }
            Thread.sleep(50);
        }


    }

}
