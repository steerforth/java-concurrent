package com.steer.concurrent.juctool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Person implements Runnable {
    Logger log = LoggerFactory.getLogger(Person.class);
    Random random = new Random();

    String name;

    MarriagePhaser phaser;

    public Person(String name,MarriagePhaser phaser) {
        this.name = name;
        this.phaser = phaser;
    }

    @Override
    public void run() {
        arrive();
        eat();
        leave();
        hug();
    }

    private void arrive() {
        millisSleep(random.nextInt(1000));
        log.info("{} 到达现场",name);
        phaser.arriveAndAwaitAdvance();
    }

    private void eat() {
        millisSleep(random.nextInt(1000));
        log.info("{} 吃完",name);
        phaser.arriveAndAwaitAdvance();
    }

    private void leave() {
        millisSleep(random.nextInt(1000));
        log.info("{} 离开",name);
        phaser.arriveAndAwaitAdvance();
    }

    private void hug() {
        if ("新郎".equals(name) || "新娘".equals(name)) {
            millisSleep(random.nextInt(1000));
            log.info("{} 准备抱抱", name);
            phaser.arriveAndAwaitAdvance();
        }else {
            phaser.arriveAndAwaitAdvance();
//            phaser.register();
        }
    }

    void millisSleep(int milli){
        try {
            TimeUnit.MILLISECONDS.sleep(milli);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
