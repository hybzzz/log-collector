package com.hybzzz.log.collector.agent;

import ch.qos.logback.classic.spi.LoggingEvent;
import com.hybzzz.log.collector.slf4j.LogCollectorMaker;

import java.io.IOException;
import java.util.concurrent.Semaphore;

public class LogFilter {

    static final Semaphore sem = new Semaphore(10);
    public static LoggingEvent doFilter(LoggingEvent event) throws IOException {
        if(event!=null){
            if(event.getMarker()!=null && event.getMarker() instanceof LogCollectorMaker){
                try {
                    sem.acquire();
                    synchronized (LogFilter.class){
                        String formattedMessage = event.getFormattedMessage();

                        JedisUtils.sendToChannel(event.getMarker(),formattedMessage);
                    }
                }catch (Exception e){
                    // ingore
                }finally {
                    sem.release();
                }
            }
        }
        return event;
    }

}