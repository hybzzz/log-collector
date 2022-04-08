package com.nti56.csplice.log.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.nti56.csplice.log.MessageHandler;
import com.nti56.csplice.log.thread.SubThread;

import java.util.concurrent.*;
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
    private static final ExecutorService LOG_ASYNC_EXECUTOR = new ThreadPoolExecutor(
            0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<>(),
            new ThreadFactoryBuilder().setNameFormat("log-worker-%d").setDaemon(true).build());

    public static void subscribe  (String url,String channel,
                                   BiConsumer<String,String> callback
    )  {// 回调
        MessageHandler handler = new MessageHandler();
        handler.setCallBack(callback);
        new SubThread(url,channel,handler).start();
    }
    public static void subscribeAsync  (String url,String channel,
                                        BiConsumer<String,String> callback
    )  {// 回调
        MessageHandler handler = new MessageHandler();
        handler.setCallBack(callback);
        LOG_ASYNC_EXECUTOR.submit(new SubThread(url,channel,handler));
    }


}
