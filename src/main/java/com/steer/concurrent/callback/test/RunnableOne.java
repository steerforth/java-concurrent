package com.steer.concurrent.callback.test;

import com.steer.concurrent.callback.MyCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RunnableOne implements Runnable {
    static Logger log = LoggerFactory.getLogger(RunnableOne.class);

    private boolean terminate;

    private MyCallback callback;

    @Override
    public void run() {

        while (!terminate){
            log.info("start do something");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("end do something");
        }

        if (callback != null){
            callback.callBack("退出线程了");
            callback = null;
        }

    }

    public void terminate(MyCallback callback){
        this.callback = callback;
        this.terminate = true;
    }

    public static void main(String[] args) {
        RunnableOne runnableOne = new RunnableOne();
        new Thread(runnableOne).start();

        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("======start=====");
        runnableOne.terminate(new MyCallback() {
            @Override
            public void callBack(Object obj) {
                //回调执行的线程和RunnableOne一致
                log.info("==============收到返回数据：=============="+obj);
            }
        });
        log.info("======end=====");

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
