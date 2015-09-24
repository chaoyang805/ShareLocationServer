package com.chaoyang805.socketserver;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * Created by chaoyang805 on 2015/9/19.
 * Mina服务器的主类
 */
public class MinaServer {
    /**
     * main方法中开启服务器端
     * @param args
     */
    public static void main(String[] args) {
        try {

            //1.实例化acceptor对象
            NioSocketAcceptor acceptor = new NioSocketAcceptor();
            //2.设置消息收发的handler对象
            acceptor.setHandler(new LocationHandler());
            //3.拦截器,添加一个新的codec拦截器
            acceptor.getFilterChain()
                    .addLast("codec", new ProtocolCodecFilter(
                            new TextLineCodecFactory(Charset.forName("UTF-8"))));
            //4.绑定到指定端口，监听客户端的连接
            acceptor.bind(new InetSocketAddress(9988));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
