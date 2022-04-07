package com.nti56.csplice.ws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
@Slf4j
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        log.info("Before Handshake");
        if (request instanceof ServletServerHttpRequest) {
            String token = ((ServletServerHttpRequest) request).getServletRequest().getParameter("token");
            String channel = ((ServletServerHttpRequest) request).getServletRequest().getParameter("channel");
            attributes.put(GlobalConst.TOKEN, token);
            attributes.put(GlobalConst.CHANNEL, channel);
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

    }
}