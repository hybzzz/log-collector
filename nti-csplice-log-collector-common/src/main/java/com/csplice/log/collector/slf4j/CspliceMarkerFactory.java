package com.csplice.log.collector.slf4j;

import org.slf4j.Marker;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
/**
 * 类说明: slf4j日志标识对象<br/>
 *
 * @author huangyubin <br/>
 * @version 1.0
 * @date 2021/10/22 15:23<br/>
 * @since JDK 1.8
 */
public class CspliceMarkerFactory {
    private final static ConcurrentMap<String, Marker> MARKER_MAP = new ConcurrentHashMap<>();


    public static Marker getMarker(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Marker name   cannot be null");
        } else {
            Marker marker = MARKER_MAP.get(name);
            if (marker == null) {
                marker = new CspliceLogMaker(name);
                Marker oldMarker = MARKER_MAP.putIfAbsent(name, marker);
                if (oldMarker != null) {
                    marker = oldMarker;
                }
            }

            return marker;
        }
    }

    public static boolean exists(String name) {
        return name != null && MARKER_MAP.containsKey(name);
    }

    public static boolean detachMarker(String name) {
        if (name == null) {
            return false;
        } else {
            return MARKER_MAP.remove(name) != null;
        }
    }

    public static Marker getDetachedMarker(String name) {
        return new CspliceLogMaker(name);
    }
}