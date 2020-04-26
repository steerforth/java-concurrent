package com.steer.concurrent.juctool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Phaser;

public class MarriagePhaser extends Phaser {
    Logger log = LoggerFactory.getLogger(MarriagePhaser.class);

    @Override
    protected boolean onAdvance(int phase, int registeredParties){
        switch (phase){
            case 0:
                log.info("所有人都到齐了 {}",registeredParties);
                return false;
            case 1:
                log.info("所有人都吃完了 {}",registeredParties);
                return false;
            case 2:
                log.info("所有人都离开了 {}",registeredParties);
                return false;
            case 3:
                log.info("婚礼结束!新郎新娘抱抱! {}",registeredParties);
                return true;
            default:
                return true;
        }
    }

}
