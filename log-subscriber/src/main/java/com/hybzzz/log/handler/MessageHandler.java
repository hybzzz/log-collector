package com.hybzzz.log.handler;

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


    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        super.onSubscribe(channel, subscribedChannels);
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        super.onUnsubscribe(channel, subscribedChannels);
    }


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
