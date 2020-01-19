package com.steer.concurrent.callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IRunnable implements Runnable{

    Logger logger = LoggerFactory.getLogger(IRunnable.class);

    SuperCallBack callback;

    @Override
    public void run() {
        logger.info("开始执行耗时任务");
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        callback = new SuperCallBack();
        callback.callBack(Thread.currentThread().getName());
        logger.info("执行完毕耗时任务");
    }

    public SuperCallBack getCallback() {
        return callback;
    }

    public void setCallback(SuperCallBack callback) {
        this.callback = callback;
    }
}
