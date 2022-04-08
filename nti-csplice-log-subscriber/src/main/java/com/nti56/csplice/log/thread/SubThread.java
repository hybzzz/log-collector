package com.nti56.csplice.log.thread;

import com.nti56.csplice.log.MessageHandler;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPooled;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;


@Slf4j
public class SubThread extends Thread {
    private String url;
    private String channel ;
    private CountDownLatch latch;

    private  JedisPooled pool ;
    private MessageHandler handler;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public MessageHandler getHandler() {
        return handler;
    }

    public void setHandler(MessageHandler handler) {
        this.handler = handler;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }


    public SubThread(String url,
                     String channel,
                     MessageHandler handler){
        this.channel = channel;
        this.url = url ;
        this.handler = handler ;
        this.pool =  new JedisPooled(url);
    }

    @Override
    public void run() {
        log.info("subscribe redis, channel :{}, thread will be blocked", channel);
        try {
            this.handler.setLastActive(System.currentTimeMillis());
            heartBeat();
            pool.subscribe(this.handler, channel);

        } catch (Exception e) {
            log.info("subscribe channel error :{}->: {}",url, e.getMessage());
        } finally {
        }
    }

    public void heartBeat(){
        SubThread subThread = this ;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                    while (handler!=null && handler.isSubscribed()){
                        if(System.currentTimeMillis() - handler.getLastActive() > 120000){
                            //超过2分钟没有收到消息
                            handler.unsubscribe();
                            log.info("超时无响应，取消订阅");
                            try {
                                synchronized (subThread){
                                    subThread.wait();
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            }
        }, 0, 1000);

    }

}