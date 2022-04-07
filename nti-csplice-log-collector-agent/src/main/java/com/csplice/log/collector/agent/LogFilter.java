package com.csplice.log.collector.agent;

import ch.qos.logback.classic.spi.LoggingEvent;
import com.csplice.log.collector.slf4j.CspliceLogMaker;

import java.io.IOException;
import java.util.List;

public class LogFilter {


    public static LoggingEvent doFilter(LoggingEvent event) throws IOException {
        System.out.println(1234);
        if(event!=null){
            if(event.getMarker()!=null && event.getMarker() instanceof CspliceLogMaker){
                String formattedMessage = event.getFormattedMessage();
                JedisUtils.sendToChannel(event.getMarker(),formattedMessage);
            }
        }
        return event;
    }

}