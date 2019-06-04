package com.steer.concurrent.threadpool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {



    public static void main(String[] args) {

        /**
         * corePoolSize:执行任务的线程   maximumPoolSize:   keepAliveTime:   队列大小：不能小于任务数-最大线程池数/核心线程池数
         */
        ExecutorService pool = new ThreadPoolExecutor(3,3,0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>(1024),new ThreadFactoryBuilder().setNameFormat("mythread-%d").build());
        for (int i = 0; i < 40; i++) {
            pool.execute(new MyRunnable(i));
        }

        pool.shutdown();

		try {
		    //每隔1秒轮询  线程池中的任务是否都完成了
			while(!pool.awaitTermination(1,TimeUnit.SECONDS)){
				System.out.println("仍有线程任务未完成");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("all over");
    }
}
