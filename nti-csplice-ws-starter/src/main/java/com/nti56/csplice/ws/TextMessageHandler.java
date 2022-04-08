package com.nti56.csplice.ws;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nimbusds.jose.JWSObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class TextMessageHandler extends TextWebSocketHandler {


    private static final Map<String, WebSocketSession> conns;

    private static final String CONN_KEY = "%s-%s-%s";
    @Autowired(required = false)
    WebSocketEvent webSocketEvent;

    static {
        conns =  new HashMap<String, WebSocketSession>();
    }

    public TextMessageHandler() {}


    /**
     * 连接成功时候，会触发页面上onopen方法
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("当前连接数:{}",conns.size());
        String token = (String) session.getAttributes().get(GlobalConst.TOKEN);
        String channel = (String) session.getAttributes().get(GlobalConst.CHANNEL);
        JSONObject userByToken = getUserInfo(token);
        log.info("创建连接成功:channel->{},userid->{},socketid->{}",channel,userByToken.getString("userId"),session.getId());
        String key = String.format(CONN_KEY,channel,userByToken.getString("userId"),session.getId());
        conns.put(key,session);
        log.info("当前连接数:{}",conns.size());
        if(webSocketEvent!=null){
            webSocketEvent.afterConnectionEstablished(session);
        }
    }

    
    @SneakyThrows
    public JSONObject getUserInfo(String token){
        JWSObject jwsObject = JWSObject.parse(token);
        String userStr = jwsObject.getPayload().toString();
        return JSONObject.parseObject(userStr);
    }
    /**
     * 关闭连接时触发
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info("关闭websocket连接:{}",session.getId());
        String token = (String) session.getAttributes().get(GlobalConst.TOKEN);
        String channel = (String) session.getAttributes().get(GlobalConst.CHANNEL);
        JSONObject userByToken = getUserInfo(token);
        log.info("用户:{}，sockerid:{} 已退出",userByToken.getString("realname"),session.getId());
        String key = String.format(CONN_KEY,channel,userByToken.getString("userId"),session.getId());
        conns.remove(key);
        log.info("剩余在线用户:{}",conns.size());
        if(webSocketEvent!=null){
            webSocketEvent.afterConnectionClosed(session,closeStatus);
        }
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
        String token = (String) session.getAttributes().get(GlobalConst.TOKEN);
        String channel = (String) session.getAttributes().get(GlobalConst.CHANNEL);
        JSONObject userByToken = getUserInfo(token);
        String key = String.format(CONN_KEY,channel,userByToken.getString("userId"),session.getId());
        log.info("用户:{}，sockerid:{} 已退出",userByToken.getString("realname"),session.getId());
        conns.remove(key);
        if(webSocketEvent!=null){
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



    /**
     * 给所有在线用户发送消息
     *
     */
    public static void sendMessageToUsers(String message, String channel) {
        if (null != message && message.length() != 0) {
            try {
                for (String key : conns.keySet()) {
                    if (key.startsWith(channel + "-")) {
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