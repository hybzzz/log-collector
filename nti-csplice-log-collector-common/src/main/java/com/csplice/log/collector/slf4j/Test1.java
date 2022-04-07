package com.csplice.log.collector.slf4j;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;

import java.io.IOException;

/**
 * 类说明: <br/>
 *
 * @author huangyubin <br/>
 * @version 1.0
 * @date 2022/4/6 13:43<br/>
 * @since JDK 1.8
 */
@Slf4j
public class Test1 {

    public static void main(String[] args) throws IOException {
//        doGetTestByNotParam();
        Marker test_marker = CspliceMarkerFactory.getMarker("module_assembly_123");
        log.info(test_marker,"ppppppasa:{}",123123);
    }
}
