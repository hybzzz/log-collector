package com.nti56.csplice.ws;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nimbusds.jose.JWSObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TextMessageHandler extends TextWebSocketHandler {


    private static final Map<String, WebSocketSession> conns;

    private static final String CONN_KEY = "%s-%s-%s";


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
        //todo 自定义回调
    }

    /**
     * js调用websocket.send时候，会调用该方法
     */
    @Override
    protected void handleTextMessage(WebSocketSession session,
                                     TextMessage textMessage) throws Exception {

        super.handleTextMessage(session, textMessage);

        String message = textMessage.getPayload();
        //todo 自定义回调




    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if(session.isOpen()){
            session.close();
        }
        log.debug("传输出现异常，关闭websocket连接... ");
        //todo 自定义回调
//        log.info("用户:{}，sockerid:{} 已退出",userByToken.getString("realname"),session.getId());
//        conns.remove(key);
    }

    @Override
    public boolean supportsPartialMessages() {
        //todo 自定义回调
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