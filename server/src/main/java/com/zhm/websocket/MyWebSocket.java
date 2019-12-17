package com.zhm.websocket;

import com.zhm.util.JsonUtil;
import com.zhm.util.websocketModel;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Author: Qzli
 * @Description:
 * @Date: Create by 2018/6/25 0023 10:54
 */
@ServerEndpoint("/api/websocket")
@Component
public class MyWebSocket {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();
    //存储二维码维一标识
    public static Map<String,Object> sessions = new HashMap<>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session)throws Exception  {
        System.out.println("连接成功;");
        this.session = session;
        //生成唯一ID
        String uuid = String.valueOf(UUID.randomUUID());
        sessions.put(uuid,this.session);  //把唯一标识跟客户端绑定
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
        try {
//            Thread.sleep(60000);
            websocketModel wm = new websocketModel();
            wm.setType("connect");
            wm.setUid(uuid);
            sendInfo(wm,uuid);
        } catch (IOException e) {
            System.out.println("IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
        System.out.println(webSocketSet);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }


    public  void sendMessage(String message,Session session) throws IOException {
        System.out.println(session);
        session.getBasicRemote().sendText(JsonUtil.toJson(message));
        //this.session.getAsyncRemote().sendText(message);
    }


    /** Qzli 2018/06/25
     * 自定义发送消息（解决广播式发送消息的问题）
     * */
    public  void sendInfo(Object message,String uid) throws IOException {
        System.out.println(webSocketSet);
        try {
            sendMessage(JsonUtil.toJson(message),(Session)sessions.get(uid));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        MyWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        MyWebSocket.onlineCount--;
    }
}