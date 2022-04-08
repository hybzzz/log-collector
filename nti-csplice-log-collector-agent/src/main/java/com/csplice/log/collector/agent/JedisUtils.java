package com.csplice.log.collector.agent;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.janetfilter.core.commons.DebugInfo;
import com.janetfilter.core.models.FilterRule;
import com.janetfilter.core.plugin.PluginConfig;
import org.slf4j.Marker;
import redis.clients.jedis.JedisPooled;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 类说明: <br/>
 *
 * @author huangyubin <br/>
 * @version 1.0
 * @date 2022/4/6 14:06<br/>
 * @since JDK 1.8
 */
public class JedisUtils {
    private static JedisPooled POOL ;

    private static String CHANNEL = "csplice_log";
    private static String REDIS = "REDIS";
    private static final ExecutorService LOG_ASYNC_EXECUTOR = new ThreadPoolExecutor(
            0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<>(),
            new ThreadFactoryBuilder().setNameFormat("log-worker-%d").setDaemon(true).build());


    public static void  sendToChannel(Marker marker, String message){
        LOG_ASYNC_EXECUTOR.submit(() -> {
            String channel = CHANNEL + "_" + marker.getName();
            long publish = POOL.publish(channel, message);
            DebugInfo.output("publish message : "+channel + ","+message);
        });
    }

    public static void initJedis(PluginConfig config){
        List<FilterRule> redisRules = config.getBySection(REDIS);
        String url = "redis://localhost:6379" ;
        if(redisRules!=null && redisRules.size()>0){
            try{
                url = redisRules.get(0).getRule();
            }catch (Exception e){
                // in
            }
            POOL = new JedisPooled(url);
        }

    }
}
