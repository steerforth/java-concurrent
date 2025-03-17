package com.steer.concurrent.leecode;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 *
 * 1117题  H2O 生成
 */
public class H2OTest {
    Logger LOGGER = LoggerFactory.getLogger(H2OTest.class);

    class H2O {

        public H2O() {

        }

        public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {

            // releaseHydrogen.run() outputs "H". Do not change or remove this line.
            releaseHydrogen.run();
        }

        public void oxygen(Runnable releaseOxygen) throws InterruptedException {

            // releaseOxygen.run() outputs "O". Do not change or remove this line.
            releaseOxygen.run();
        }
    }

    class releaseHydrogen implements Runnable{
        @Override
        public void run() {
            LOGGER.info("H");
        }
    }

    class releaseOxygen implements Runnable{
        @Override
        public void run() {
            LOGGER.info("O");
        }
    }

    @Test
    public void test(){
        ExecutorService pool = new ThreadPoolExecutor(3,3,0L, TimeUnit.SECONDS,new LinkedBlockingQueue<>(3),new ThreadFactoryBuilder().setNameFormat("mythread-%d").build());
        Future<?> h1Submit = pool.submit(new releaseHydrogen());
        Future<?> h2Submit = pool.submit(new releaseHydrogen());
        Future<?> o1Submit = pool.submit(new releaseOxygen());

        boolean h1Done = h1Submit.isDone();
        boolean h2Done = h2Submit.isDone();
        boolean o1Done = o1Submit.isDone();

        LOGGER.info(">>>>{} {} {}",h1Done,h2Done,o1Done);
    }
}
