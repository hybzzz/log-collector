package com.hybzzz.websocket;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
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
 * @date 2022/4/8 12:24<br/>
 * @since JDK 1.8
 */
public interface WebSocketEvent {
    void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e);
    boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                            Map<String, Object> attributes);
    void handleTextMessage(WebSocketSession session,
                           TextMessage textMessage);
    boolean supportsPartialMessages();
    void handleTransportError(WebSocketSession session, Throwable exception);
    void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus);
    void afterConnectionEstablished(WebSocketSession session);
    String getConnKey(WebSocketSession session);
}
