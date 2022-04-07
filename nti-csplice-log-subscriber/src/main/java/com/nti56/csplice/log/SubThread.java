package com.nti56.csplice.log;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPooled;

import java.util.concurrent.CountDownLatch;
import java.util.function.BiConsumer;


@Slf4j
public class SubThread extends Thread {
    private String ip;
    private int port;
    private String channel ;
    private CountDownLatch latch;

    private  JedisPooled pool ;
    private BiConsumer<String,String> callBack;
    private static  final MessageHandler SUB = new MessageHandler();
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public BiConsumer<String,String> getCallBack() {
        return callBack;
    }

    public void setCallBack(BiConsumer<String,String> callBack) {
        this.callBack = callBack;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }

    public SubThread(String ip, int port, String channel){
        this.channel = channel;
        this.port = port;
        this.ip = ip ;
        this.pool =  new JedisPooled(ip, port);
    }

    public SubThread(String ip, int port,
                     String channel,
                     BiConsumer<String,String> callback){
        this.channel = channel;
        this.port = port;
        this.ip = ip ;
        this.callBack = callback ;
        this.pool =  new JedisPooled(ip, port);
    }

    @Override
    public void run() {
        log.info("subscribe redis, channel :{}, thread will be blocked", channel);
        try {
            SUB.setCallBack(this.getCallBack());
            pool.subscribe(SUB, channel);
        } catch (Exception e) {
            log.info("subscribe channel error : {}", e.getMessage());
        } finally {
        }
    }

}