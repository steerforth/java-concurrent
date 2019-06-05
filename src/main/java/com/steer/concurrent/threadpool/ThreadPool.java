package com.steer.concurrent.threadpool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.*;

public class ThreadPool {

	private static void executorServicetest(){
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

	/**
	 * 延迟执行，只执行1次任务
	 */
	private static void scheduledExecutorServiceTest(){
		ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(4,new BasicThreadFactory.Builder().namingPattern("").daemon(false).build());
		scheduler.schedule(new MyRunnable(1),2,TimeUnit.SECONDS);
	}

	/**
	 * 延迟2秒，周期1秒，
	 */
	private static void scheduledExecutorServiceTest2(){
		ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(4,new BasicThreadFactory.Builder().namingPattern("").daemon(false).build());
		scheduler.scheduleAtFixedRate(new MyRunnable(1),2,1,TimeUnit.SECONDS);
//		scheduler.shutdown();
//		scheduler.shutdownNow();
	}

    public static void main(String[] args) {
//		executorServicetest();
		scheduledExecutorServiceTest2();
    }
}
