package com.liaoyin.lyproject.socket.main;

import com.liaoyin.lyproject.socket.service.ChannelUtil;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @ClassName SocketAcceptor
 * @Title 套接字接收器
 * @Author Set
 * @Date 2018/8/19 10:51
 * @Description
 **/
public class SocketAcceptor implements Runnable {

    private final ServerSocketChannel serverChannel;

    private final Selector selector;

    private DataNotice notice;

    public SocketAcceptor(Selector selector, ServerSocketChannel serverChannel, DataNotice notice) {
        this.serverChannel = serverChannel;
        this.selector = selector;
        this.notice = notice;
    }

    @Override
    public void run() {
        try {
            //接收客户端
            SocketChannel socketChannel = serverChannel.accept();
            if(socketChannel != null) {
                SocketSession session = new SocketSession(socketChannel);
                //非阻塞
                socketChannel.configureBlocking(false);
                //注册OP_READ事件 返回通道
                SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
                //使阻塞的Selector立即返回
                selector.wakeup();
                //给key附带一个管理器对象
                selectionKey.attach(new SocketController(selectionKey, session, notice));
                ChannelUtil.addChannel(session);
                System.out.println("================================== 设备加入 ==================================");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
