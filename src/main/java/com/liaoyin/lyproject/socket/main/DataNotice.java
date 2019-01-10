package com.liaoyin.lyproject.socket.main;

import java.nio.ByteBuffer;

/**
 * @ClassName DataListener
 * @Title 数据通知接口
 * @Author Set
 * @Date 2018/8/19 12:38
 * @Description
 **/
public interface DataNotice {
    /**
     * 数据接收
     * @param buffer
     */
    void receive(ByteBuffer buffer, SocketSession session);
}
