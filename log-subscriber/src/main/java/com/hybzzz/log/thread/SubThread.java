package com.hybzzz.log.thread;

import com.hybzzz.log.handler.MessageHandler;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPooled;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;


@Slf4j
public class SubThread extends Thread {
    private String url;
    private String[] channels ;
    private CountDownLatch latch;

    private  JedisPooled pool ;
    private MessageHandler handler;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String[] getChannels() {
        return channels;
    }

    public void setChannels(String[] channels) {
        this.channels = channels;
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
                     MessageHandler handler,
                     String... channels
                     ){
        this.channels = channels;
        this.url = url ;
        this.handler = handler ;
        this.pool =  new JedisPooled(url);
    }

    @Override
    public void run() {
        log.info("subscribe redis, channel :{}, thread will be blocked", channels);
        try {
            this.handler.setLastActive(System.currentTimeMillis());
            pool.subscribe(this.handler, channels);

        } catch (Exception e) {
            log.info("subscribe channel error :{}->: {}",url, e.getMessage());
        } finally {
        }
    }


}