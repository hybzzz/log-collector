package com.hybzzz.websocket;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
@Slf4j
@Component
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {

        if(SpringUtil.getBean(WebSocketEvent.class)!=null){
            return SpringUtil.getBean(WebSocketEvent.class).beforeHandshake(request,response,wsHandler,attributes);
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest,
                               ServerHttpResponse serverHttpResponse,
                               WebSocketHandler webSocketHandler, Exception e) {
        if(SpringUtil.getBean(WebSocketEvent.class)!=null){
            SpringUtil.getBean(WebSocketEvent.class).afterHandshake(serverHttpRequest,
                    serverHttpResponse,webSocketHandler,e);
        }
    }
}