package com.csplice.log.collector.slf4j;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.spi.AppenderAttachableImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Marker;
import sun.net.www.http.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.MarkerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.locks.ReentrantLock;

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
    public static void main(String[] args) throws IOException {
//        doGetTestByNotParam();
        Marker test_marker = CspliceMarkerFactory.getMarker("test_marker");
        log.info(test_marker,"CloseableHttpClient:{}",123123);
    }


//        1. 永远只在更新对象的成员变量时加锁
//        2. 永远只在访问可变的成员变量时加锁
//        3. 永远不在调用其他对象的方法时加锁
    /**
     * 无参GET请求
     */
    public static String doGetTestByNotParam() throws IOException {
        URL url = new URL("http://www.baidu.com");
        URLConnection urlConnection = url.openConnection();
        HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;
        int responseCode = httpUrlConnection.getResponseCode();
        System.out.println(responseCode);
        System.out.println(httpUrlConnection.getResponseMessage());
        return "";
    }

}
