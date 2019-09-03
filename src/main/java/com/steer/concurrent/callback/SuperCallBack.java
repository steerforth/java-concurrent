package com.steer.concurrent.callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SuperCallBack implements MyCallback{
    Logger LOGGER = LoggerFactory.getLogger(SuperCallBack.class);
    @Override
    public void callBack(Object obj) {
        LOGGER.info("我是回调信息：{}",obj);
    }
}
