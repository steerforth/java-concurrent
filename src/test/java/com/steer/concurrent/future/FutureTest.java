package com.steer.concurrent.future;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.*;

public class FutureTest {
    private Logger log = LoggerFactory.getLogger(FutureTest.class);
    @Test
    public void test() throws IOException {
        log.info("====start to caculate====");
        IFuture iFuture = new DelayAdder().add(4,5,5000).addListener(new IFutureListener<Integer>() {
            @Override
            public void onComplete(IFuture<Integer> future) {
                log.info("====caculate result:{}====",future.getNow());
            }
        });
        log.info("=------"+iFuture.getNow());
        try {
            log.info("=------"+iFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        log.info("====end to caculate====");
        System.in.read();
    }

    @Test
    public void test2() throws IOException {
        ExecutorService pool = new ThreadPoolExecutor(6,10,0L, TimeUnit.SECONDS,new LinkedBlockingQueue<>(44),new ThreadFactoryBuilder().setNameFormat("mythread-%d").build());
        log.info("====start to caculate====");
        for (int i = 0; i < 20; i++) {
            MyFuture future = new MyFuture();
            future.addListener((IFuture<Integer> f)->{
                log.info("====caculate result:{}====",f.getNow());
            });
            pool.submit(new MyRunnable(i,i+3,3000,future));
        }
        log.info("====end to caculate====");
        System.in.read();
    }

    private class DelayAdder{
        public MyFuture add(int a,int b,long deday){
            MyFuture future = new MyFuture();
            new Thread(new MyRunnable(a,b,deday,future)).start();
            return future;
        }
    }

    private class MyRunnable implements Runnable{
        private long delay;

        private int a,b;

        private MyFuture future;

        public MyRunnable(int a, int b, long delay, MyFuture future) {
            this.delay = delay;
            this.a = a;
            this.b = b;
            this.future = future;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(delay);
                int c = a+b;
                future.setSuccess(c);
            } catch (InterruptedException e) {
                e.printStackTrace();
                future.setFailure(e);
            }
        }
    }

    private class MyFuture extends AbstractFuture<Integer>{
        @Override
        public IFuture<Integer> setSuccess(Object result){
            return super.setSuccess(result);
        }

        @Override
        public IFuture<Integer> setFailure(Throwable cause){
            return super.setFailure(cause);
        }
    }
}
