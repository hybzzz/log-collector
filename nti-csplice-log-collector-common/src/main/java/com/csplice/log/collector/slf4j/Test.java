package com.csplice.log.collector.slf4j;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MarkerFactory;

/**
 * 类说明: <br/>
 *
 * @author huangyubin <br/>
 * @version 1.0
 * @date 2021/10/22 15:39<br/>
 * @since JDK 1.8
 */
@Slf4j
public class Test {
    public static void main(String[] args) {
        log.info(CspliceMarkerFactory.getMarker("test_marker"),"asdasdasd");
    }
}
