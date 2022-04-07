package com.csplice.log.collector.agent;

import ch.qos.logback.classic.spi.LoggingEvent;
import com.csplice.log.collector.slf4j.CspliceLogMaker;
import com.janetfilter.core.models.FilterRule;

import java.io.IOException;
import java.util.List;

public class LogFilter {
    private static List<FilterRule> ruleList;

    public static void setRules(List<FilterRule> rules) {
        ruleList = rules;
    }


    public static LoggingEvent doFilter(LoggingEvent event) throws IOException {
        if(event!=null){
            if(event.getMarker()!=null && event.getMarker() instanceof CspliceLogMaker){
                String formattedMessage = event.getFormattedMessage();
                JedisUtils.sendToChannel(event.getMarker(),formattedMessage);
            }
        }
        return event;
    }

}