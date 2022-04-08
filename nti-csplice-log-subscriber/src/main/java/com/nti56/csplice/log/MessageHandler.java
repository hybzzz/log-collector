package com.nti56.csplice.log;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPubSub;

import java.util.function.BiConsumer;

/**
 * 类说明: <br/>
 *
 * @author huangyubin <br/>
 * @version 1.0
 * @date 2022/4/6 16:44<br/>
 * @since JDK 1.8
 */
@Slf4j
public class MessageHandler extends JedisPubSub {

    private BiConsumer<String,String> callBack;


    public void setCallBack(BiConsumer<String,String> callBack) {
        this.callBack = callBack;
    }

    private Long lastActive = 0L;

    public Long getLastActive() {
        return lastActive;
    }

    public void setLastActive(Long lastActive) {
        this.lastActive = lastActive;
    }

    @Override
    public void onMessage(String channel, String message) {
        lastActive = System.currentTimeMillis();
        super.onMessage(channel, message);
        log.info("receive message from channel : {} -> :{}",channel,message);
        if(callBack!=null){
            callBack.accept(channel,message);
        }
    }
}
