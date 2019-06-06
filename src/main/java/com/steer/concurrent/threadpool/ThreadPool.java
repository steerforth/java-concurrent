package com.steer.concurrent.threadpool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.*;

public class ThreadPool {
	/**
	 * 当线程池和缓冲队列都北塞满了任务，但还有新的任务时，就会执行拒绝策略
	 * 即执行的任务数量大于（最大线程池大小+缓冲队列大小）
	 * 线程池的几种拒绝策略：
	 * ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException
	 * ThreadPoolExecutor.DiscardPolicy:丢弃任务，但是不抛出异常
	 * ThreadPoolExecutor.DiscardOldestPolicy:丢弃队列最前面的任务，然后重新尝试执行任务(重复此过程)
	 * ThreadPoolExecutor.CallerRunsPolicy:由调用线程处理该任务
	 */

	private static void executorServicetest(){
		/**
		 * corePoolSize:执行任务的线程
		 * maximumPoolSize: 线程池最大数量
		 * keepAliveTime:  空闲线程存活时间，线程没有任务执行时最多保持多久时间
		 * 缓冲队列大小：大于corePoolSize的任务会放在缓冲队列中，
		 * 所以值大于等于任务数-最大线程池数
		 * ArrayBlockingQueue
		 * LinkedBlokingQueue
		 * SynchronousQueue
		 * ArrayBlockingQueue
		 * PriotityBlockingQueue
		 */
		ExecutorService pool = new ThreadPoolExecutor(3,10,0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>(40),new ThreadFactoryBuilder().setNameFormat("mythread-%d").build());
		for (int i = 0; i <50; i++) {
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
		ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(4,new BasicThreadFactory.Builder().namingPattern("mythread").daemon(false).build());
		scheduler.schedule(new MyRunnable(1),2,TimeUnit.SECONDS);
	}

	/**
	 * 延迟2秒，周期1秒，
	 */
	private static void scheduledExecutorServiceTest2(){
		ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(4,new BasicThreadFactory.Builder().namingPattern("mythread").daemon(false).build());
		scheduler.scheduleAtFixedRate(new MyRunnable(1),2,1,TimeUnit.SECONDS);
//		scheduler.shutdown();
//		scheduler.shutdownNow();
	}

    public static void main(String[] args) {
		executorServicetest();
//		scheduledExecutorServiceTest2();
    }
}
