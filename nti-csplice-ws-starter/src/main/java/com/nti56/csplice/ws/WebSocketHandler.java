//package com.nti56.csplice.ws;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.nimbusds.jose.JWSObject;
//import com.nti56.report.util.HttpUtils;
//import com.nti56.report.util.PakoGzipUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//import java.io.IOException;
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Slf4j
//public class WebSocketHandler extends TextWebSocketHandler {
//
//
//    private static final Map<String, WebSocketSession> conns;
//
//    private static final String TOKEN = "token";
//
//    private static final String CONN_KEY = "%s-%s-%s";
//
//
//    static {
//        conns =  new HashMap<String, WebSocketSession>();
//    }
//
//    public WebSocketHandler() {}
//
//    public JSONObject getUserByToken(String token ){
//        try {
//            JWSObject jwsObject  = JWSObject.parse(token);
//            String userStr = jwsObject.getPayload().toString();
//            return JSONObject.parseObject(userStr);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return null;
//
//    }
//
//    /**
//     * 连接成功时候，会触发页面上onopen方法
//     */
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        String token = (String) session.getAttributes().get(TOKEN);
//        String reportId = (String) session.getAttributes().get("reportId");
//        JSONObject userByToken = getUserByToken(token);
//        log.info("创建连接成功:reportid->{},userid->{},socketid->{}",reportId,userByToken.getString("userId"),session.getId());
//        String key = String.format(CONN_KEY,reportId,userByToken.getString("userId"),session.getId());
//        conns.put(key,session);
//        log.info("当前连接数:{}",conns.size());
//    }
//
//    /**
//     * 关闭连接时触发
//     */
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
//        log.info("关闭websocket连接:{}",session.getId());
//
//        String token = (String) session.getAttributes().get(TOKEN);
//        String reportId = (String) session.getAttributes().get("reportId");
//        JSONObject userByToken = getUserByToken(token);
//        log.info("用户:{}，sockerid:{} 已退出",userByToken.getString("realname"),session.getId());
//        String key = String.format(CONN_KEY,reportId,userByToken.getString("userId"),session.getId());
//        conns.remove(key);
//        log.info("剩余在线用户:{}",conns.size());
//    }
//
//    /**
//     * js调用websocket.send时候，会调用该方法
//     */
//    @Override
//    protected void handleTextMessage(WebSocketSession session,
//                                     TextMessage textMessage) throws Exception {
//
//        super.handleTextMessage(session, textMessage);
//
//        String message = textMessage.getPayload();
//
//        if (message.length() != 0) {
//            try {
//                if ("rub".equals(message)) {
//                    return;
//                }
//                String unMessage = PakoGzipUtils.unCompressURI(message);
//                String token = session.getAttributes().get("token").toString();
//                JSONObject userByToken = getUserByToken(token);
//                String reportId = session.getAttributes().get("reportId").toString();
//                sendMessageToUsers(unMessage,reportId,userByToken);
//            } catch (Exception e) {
//                log.error(e.getMessage());
//            }
//        }
//
//
//
//    }
//
//    @Override
//    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
//        if(session.isOpen()){
//            session.close();
//        }
//        log.debug("传输出现异常，关闭websocket连接... ");
//        String token = (String) session.getAttributes().get(TOKEN);
//        String reportId = (String) session.getAttributes().get("reportId");
//        JSONObject userByToken = getUserByToken(token);
//        String key = String.format(CONN_KEY,reportId,userByToken.getString("userId"),session.getId());
//
//        log.info("用户:{}，sockerid:{} 已退出",userByToken.getString("realname"),session.getId());
//        conns.remove(key);
//    }
//
//    @Override
//    public boolean supportsPartialMessages() {
//
//        return false;
//    }
//
//
//
//    /**
//     * 给所有在线用户发送消息
//     *
//     */
//    public void sendMessageToUsers(String message, String reportId, JSONObject user) {
//        if (null != message && message.length() != 0) {
//            try {
//                JSONObject jsonObject = JSON.parseObject(message);
//                String userId = user.getString("userId");
//                for (String key:conns.keySet() ) {
//                    if(key.startsWith(reportId+"-")&&
//                            !key.startsWith(String.format(CONN_KEY,reportId,userId,""))){
//                        if ("mv".equals(jsonObject.getString("t")) ) {
//                            TextMessage t = new TextMessage(JSON.toJSONString(new ResponseDTO(3, userId,
//                                    user.getString("realname"), message)));
//                            conns.get(key).sendMessage(t);
//                        }else if(!"shs".equals(jsonObject.getString("t")) ){
//                            TextMessage t = new TextMessage(JSON.toJSONString(new ResponseDTO(2, userId,
//                                    user.getString("realname"), message)));
//                            conns.get(key).sendMessage(t);
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                log.error(e.getMessage());
//            }
//        }
//
//
//
//
//    }
//
//}