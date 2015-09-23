package com.chaoyang805.socketserver;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chaoyang805 on 2015/9/19.
 * 处理消息的hanlder类
 */
public class LocationHandler extends IoHandlerAdapter {
    /**
     * 保存已经连接上的客户端的session对象
     */
    private List<IoSession> mSessionList = new ArrayList<>();

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        //会话创建时将新的session对象加入到list中
        mSessionList.add(session);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        //从session中取得相关联的
        String deviceId = (String) session.getAttribute("deviceId");
        String userName = (String) session.getAttribute("userName");
        //将关闭的session对象从list中移除
        mSessionList.remove(session);
        //给其他的客户端发送消息，用deviceId和userName作为标志告诉其他人哪个客户端断开了
        for (IoSession ioSession : mSessionList) {
            ioSession.write("SESSIONCLOSED" + " " + deviceId + " " + userName);
        }
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        //分解客户端发送过来的消息的每个字段
        String theMessage = (String) message;
        String[] results = theMessage.split(" ");
        String action = results[0];
        //根据action判断消息类型
        switch (action) {
            case "CREATE":
                //会话创建时为session对象设置attribute属性
                String deviceId = results[1];
                String userName = results[2];
                session.setAttribute("deviceId", deviceId);
                session.setAttribute("userName", userName);
                break;
            case "BROADCAST":
                //遍历mSessionList,将消息发送到每一个客户端
                for (int i = 0; i < mSessionList.size(); i++) {
                    mSessionList.get(i).write(theMessage);
                }
                break;
        }

    }

}
