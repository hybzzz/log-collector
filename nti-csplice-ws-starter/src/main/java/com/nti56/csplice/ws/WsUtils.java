package com.nti56.csplice.ws;

import com.alibaba.fastjson.JSONObject;

/**
 * 类说明: <br/>
 *
 * @author huangyubin <br/>
 * @version 1.0
 * @date 2022/4/6 18:08<br/>
 * @since JDK 1.8
 */
public class WsUtils {

    public static  void sendMessageToChannelUsers(){
        // 新建工程 引入订阅者插件，引入wsstarter ， 调用订阅，回调中调用ws的发送
        TextMessageHandler.sendMessageToUsers("","");
    }


}
