package com.hybzzz.log.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.hybzzz.log.handler.MessageHandler;
import com.hybzzz.log.thread.PSubThread;
import com.hybzzz.log.thread.SubThread;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;


/**
 * 类说明: <br/>
 *
 * @author huangyubin <br/>
 * @version 1.0
 * @date 2022/4/6 17:40<br/>
 * @since JDK 1.8
 */
public class LogSubUtils {
    private static final ThreadPoolExecutor LOG_ASYNC_EXECUTOR = new ThreadPoolExecutor(
            0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<>(),
            new ThreadFactoryBuilder().setNameFormat("log-worker-%d").setDaemon(true).build());

    /**
     * 订阅
     * @param url redis url
     * @param channel 频道
     * @param callback 消费后回调
     * @return
     */
    public static MessageHandler subscribe  (String url,
                                        BiConsumer<String,String> callback,
                                             String... channels)  {// 回调
        MessageHandler handler = new MessageHandler();
        handler.setCallBack(callback);
        LOG_ASYNC_EXECUTOR.submit(new SubThread(url,handler,channels));
        return handler;
    }


    public static MessageHandler psubscribe  (String url,
                                             BiConsumer<String,String> callback,
                                             String... channels)  {// 回调
        MessageHandler handler = new MessageHandler();
        handler.setCallBack(callback);
        LOG_ASYNC_EXECUTOR.submit(new PSubThread(url,handler,channels));
        return handler;
    }
    public static int getCount(){
        return LOG_ASYNC_EXECUTOR.getActiveCount();
    }


}
