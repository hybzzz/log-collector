package com.csplice.log.collector.slf4j;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.JedisPubSub;


public class SubThread extends Thread {
    private static JedisPooled POOL = new JedisPooled("139.159.194.101", 21181);
    private final JedisPubSub subscriber = new JedisPubSub() {
        @Override
        public void onMessage(String channel, String message) {
            super.onMessage(channel, message);
            System.out.println(channel);
            System.out.println(message);
        }
    };

    private final String channel = "csplice_log_test_module_assembly_123";

    @Override
    public void run() {
        System.out.println(String.format("subscribe redis, channel %s, thread will be blocked", channel));
        try {
            POOL.subscribe(subscriber, channel);
        } catch (Exception e) {
            System.out.println(String.format("subsrcibe channel error, %s", e));
        } finally {
        }
    }

    public static void main(String[] args) {
        SubThread s = new SubThread();
        s.start();
    }
}