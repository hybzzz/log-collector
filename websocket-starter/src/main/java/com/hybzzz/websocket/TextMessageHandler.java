package com.hybzzz.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Component
public class TextMessageHandler extends TextWebSocketHandler {



    private static final Map<String, WebSocketSession> conns;

    @Autowired(required = false)
    WebSocketEvent webSocketEvent;

    static {
        conns = new HashMap<>();
    }

    public TextMessageHandler() {}


    /**
     * 连接成功时候，会触发页面上onopen方法
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("当前连接数:{}",conns.size());
        if(webSocketEvent!=null){
            String connKey = webSocketEvent.getConnKey(session);
            conns.put(connKey,session);
            webSocketEvent.afterConnectionEstablished(session);
        }
        log.info("当前连接数:{}",conns.size());
    }


    /**
     * 关闭连接时触发
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info("关闭websocket连接:{}",session.getId());
        if(webSocketEvent!=null){
            String connKey = webSocketEvent.getConnKey(session);
            conns.remove(connKey);
            webSocketEvent.afterConnectionClosed(session,closeStatus);
        }
        log.info("剩余在线用户:{}",conns.size());
    }

    /**
     * js调用websocket.send时候，会调用该方法
     */
    @Override
    protected void handleTextMessage(WebSocketSession session,
                                     TextMessage textMessage) throws Exception {

        super.handleTextMessage(session, textMessage);

        if(webSocketEvent!=null){
            webSocketEvent.handleTextMessage(session,textMessage);
        }



    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if(session.isOpen()){
            session.close();
        }
        log.debug("传输出现异常，关闭websocket连接... ");
        if(webSocketEvent!=null){
            String connKey = webSocketEvent.getConnKey(session);
            conns.remove(connKey);
            webSocketEvent.handleTransportError(session,exception);
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        if(webSocketEvent!=null){
            return webSocketEvent.supportsPartialMessages();
        }
        return false;
    }



    public static long countChannelCon(String channelPattern){
        Pattern pattern = Pattern.compile(channelPattern);
        return conns.keySet().stream().filter(
                (k) -> pattern.matcher(k).find() ).count();
    }

    /**
     * 给所有在线用户发送消息
     *
     */
    public static void sendMessageToUsers(String message, String channelPattern) {
        Pattern pattern = Pattern.compile(channelPattern);

        if (null != message && message.length() != 0) {
            try {
                for (String key : conns.keySet()) {
                    if(pattern.matcher(key).find()){
                        TextMessage t = new TextMessage(message);
                        conns.get(key).sendMessage(t);
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

}