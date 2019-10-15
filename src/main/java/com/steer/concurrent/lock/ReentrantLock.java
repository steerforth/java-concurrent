package com.steer.concurrent.lock;

import java.util.concurrent.locks.Condition;

public class ReentrantLock {
    //1.公平锁是指当锁可用时,在锁上等待时间最长的线程将获得锁的使用权。而非公平锁则随机分配这种使用权,非公平锁性能更好
    //2.可重入锁，同一个线程可多次获取同一把锁，但相应得也要释放获取的次数

}
