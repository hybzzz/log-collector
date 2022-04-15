package com.demo;

import com.alibaba.fastjson.JSONObject;
import com.hybzzz.websocket.WebSocketEvent;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

/**
 * 类说明: <br/>
 *
 * @author huangyubin <br/>
 * @version 1.0
 * @date 2022/4/8 12:31<br/>
 * @since JDK 1.8
 */
@Configuration
public class TestWsEvent implements WebSocketEvent {
    public final static String TOKEN = "token";
    public final static String CHANNEL = "channel";
    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest) {
            String token = ((ServletServerHttpRequest) request).getServletRequest().getParameter("token");
            String channel = ((ServletServerHttpRequest) request).getServletRequest().getParameter("channel");
            attributes.put(TOKEN, token);
            attributes.put(CHANNEL, channel);
        }
        return true;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage textMessage) {

        System.out.println(textMessage.getPayload());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {

    }
    private static final String CONN_KEY = "%s-%s-%s";

    @Override
    public String getConnKey(WebSocketSession session) {
        String token = (String) session.getAttributes().get(TOKEN);
        String channel = (String) session.getAttributes().get(CHANNEL);
        JSONObject userByToken = getUserInfo(token);
        String key = String.format(CONN_KEY,channel,userByToken.getString("userId"),session.getId());
        return key;
    }

    @SneakyThrows
    public JSONObject getUserInfo(String token){
//        JWSObject jwsObject = JWSObject.parse(token);
//        String userStr = jwsObject.getPayload().toString();
        return JSONObject.parseObject("{}");
    }
}
