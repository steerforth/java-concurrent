package com.steer.concurrent.jucTool;

import com.steer.concurrent.juctool.MarriagePhaser;
import com.steer.concurrent.juctool.Person;
import org.junit.Test;

import java.io.IOException;

/**
 * 阶段
 */
public class PhaserTest {
    @Test
    public void test(){
        MarriagePhaser phaser = new MarriagePhaser();
        phaser.bulkRegister(7);
        for (int i = 0; i < 5; i++) {
            new Thread(new Person("p"+i,phaser)).start();
        }

        new Thread(new Person("新郎",phaser)).start();
        new Thread(new Person("新娘",phaser)).start();

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
