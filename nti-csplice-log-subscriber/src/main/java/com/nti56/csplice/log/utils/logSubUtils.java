package com.nti56.csplice.log.utils;

import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.nti56.csplice.log.SubThread;

import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 类说明: <br/>
 *
 * @author huangyubin <br/>
 * @version 1.0
 * @date 2022/4/6 17:40<br/>
 * @since JDK 1.8
 */
public class logSubUtils {
    private static final ExecutorService LOG_ASYNC_EXECUTOR = new ThreadPoolExecutor(
            0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<>(),
            new ThreadFactoryBuilder().setNameFormat("log-worker-%d").setDaemon(true).build());

    public static void subscribe  (String ip, int port,String channel,
                                   BiConsumer<String,String> callback
    )  {// 回调
        new SubThread(ip,port,channel,callback).start();
    }
    public static void subscribeAsync  (String ip, int port,String channel,
                                   BiConsumer<String,String> callback
    )  {// 回调
        LOG_ASYNC_EXECUTOR.submit(new SubThread(ip,port,channel,callback));
    }


}
