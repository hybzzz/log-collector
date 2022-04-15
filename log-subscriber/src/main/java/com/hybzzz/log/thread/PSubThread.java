package com.hybzzz.log.thread;

import com.hybzzz.log.handler.MessageHandler;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPooled;

import java.util.concurrent.CountDownLatch;


@Slf4j
public class PSubThread extends Thread {
    private String url;
    private String[] channelPatterns ;
    private CountDownLatch latch;

    private  JedisPooled pool ;
    private MessageHandler handler;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String[] getChannelPatterns() {
        return channelPatterns;
    }

    public void setChannelPatterns(String[] channelPatterns) {
        this.channelPatterns = channelPatterns;
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


    public PSubThread(String url,
                      MessageHandler handler,
                      String... channelPatterns){
        this.channelPatterns = channelPatterns;
        this.url = url ;
        this.handler = handler ;
        this.pool =  new JedisPooled(url);
    }

    @Override
    public void run() {
        log.info("subscribe redis, channel :{}, thread will be blocked", channelPatterns);
        try {
            this.handler.setLastActive(System.currentTimeMillis());
            pool.psubscribe(this.handler, channelPatterns);

        } catch (Exception e) {
            log.info("subscribe channel error :{}->: {}",url, e.getMessage());
        } finally {
        }
    }


}